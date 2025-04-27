package pt.isel.meic.iesd.tplm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import pt.isel.meic.iesd.rnm.IReliableNodeManagerTPLM;
import pt.isel.meic.iesd.rnm.ReliableNodeManagerTPLMService;
import pt.isel.meic.iesd.rnm.Lock;

/**
 * TwoPhaseLockManager (TPLM) manages locks across distributed Resource Managers (RMs),
 * ensuring transactional access to vector elements using a two-phase locking protocol.
 * <p>
 * It verifies RM availability, grants locks, queues pending requests, and
 * communicates lock grants asynchronously through RabbitMQ.
 */
@WebService(endpointInterface = "pt.isel.meic.iesd.tplm.ITwoPhaseLockManager")
public class TwoPhaseLockManager implements ITwoPhaseLockManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwoPhaseLockManager.class);

    String basePath;
    String holderSuffix;
    String waitingSuffix;
    String pendingLocksPath;
    String locksHeldPath;

    private final IReliableNodeManagerTPLM rnm;
    private final Channel rabbitChannel;


    /**
     * Constructs a TwoPhaseLockManager instance, setting up configuration and RabbitMQ connection.
     *
     * @param rabbitMQHost the RabbitMQ server address for lock grant notifications.
     * @param rabbitMQPort the RabbitMQ server port.
     * @throws Exception if setup fails.
     */
    public TwoPhaseLockManager(String rabbitMQHost, int rabbitMQPort) throws IOException, TimeoutException {
        loadConfig();
        ReliableNodeManagerTPLMService rnmService = new ReliableNodeManagerTPLMService();
        rnm = rnmService.getReliableNodeManagerTPLMPort();
        rabbitChannel = setupRabbiMQChannel(rabbitMQHost, rabbitMQPort);
    }

    /**
     * Initializes the RabbitMQ channel for communication.
     *
     * @param rabbitMQHost the RabbitMQ server address.
     * @param rabbitMQPort the RabbitMQ server port.
     * @return the RabbitMQ channel.
     * @throws IOException      if an I/O error occurs during channel setup.
     * @throws TimeoutException if the connection times out.
     */
    private Channel setupRabbiMQChannel(String rabbitMQHost, int rabbitMQPort) throws IOException, TimeoutException {
        final Channel rabbitChannel;
        LOGGER.info("Starting RabbitMQ Client...");
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitMQHost);
            factory.setPort(rabbitMQPort);
            Connection connection = factory.newConnection();
            rabbitChannel = connection.createChannel();
            LOGGER.info("RabbitMQ session established.");
        } catch (Exception e) {
            LOGGER.error("Failed to setup connection to rabbitMQ broker", e);
            throw new RuntimeException(e);
        }

        return rabbitChannel;
    }

    /**
     * Loads TPLM configuration properties from the `tplm.properties` file.
     *
     * @throws RuntimeException if the configuration cannot be loaded.
     */
    private void loadConfig() {
        LOGGER.info("Loading TPLM configuration...");
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("tplm.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find tplm.properties");
            }
            prop.load(input);
            basePath = prop.getProperty("locks.base_path");
            holderSuffix = prop.getProperty("locks.holder_suffix");
            waitingSuffix = prop.getProperty("locks.waiting_suffix");
            pendingLocksPath = prop.getProperty("locks.pending_base_path");
            locksHeldPath = prop.getProperty("locks.held_base_path");
        } catch (Exception ex) {
            LOGGER.error("Error loading TPLM configuration", ex);
        }
        LOGGER.info("TPLM configuration loaded successfully.");
    }

    /**
     * Checks if all requested locks are currently available (not held).
     *
     * @param vectorPositions list of locks requested.
     * @return true if all locks can be granted, false otherwise.
     * @throws Exception if ZooKeeper access fails.
     */
    private boolean canGrantAll(List<Lock> vectorPositions) throws Exception {
        for (Lock lock : vectorPositions) {
            String holderPath = basePath + "/" + lock.vectorId + "/" + lock.element + "/holder";
            String holder = rnm.getHolder(holderPath, lock.vectorId, lock.element);
            if (holder != null) {
                return false; // lock already held
            }
        }
        return true;
    }

    /**
     * Creates the necessary znodes to mark locks as held by a transaction.
     *
     * @param txnID           the transaction ID holding the locks.
     * @param vectorPositions list of locks to assign.
     * @throws Exception if znode creation fails.
     */
    private void holdLocks(String txnID, List<Lock> vectorPositions) throws Exception {
        for (Lock lock : vectorPositions) {
            String holderPath = basePath + "/" + lock.vectorId + "/" + lock.element + "/holder";
            rnm.setHolder(holderPath, lock.vectorId, lock.element, txnID);
            rnm.addLockHeld(txnID, lock); // Track locks held by txn
        }
    }

    /**
     * Sends a lock request notification message to the transaction.
     *
     * @param txnID the transaction receiving the lock grant notification.
     * @param msg   the message content.
     * @throws IOException if RabbitMQ publishing fails.
     */
    private void sendLockRequestMessage(String txnID, String msg) throws IOException {
        rabbitChannel.queueDeclare(txnID, false, false, false, null);
        rabbitChannel.basicPublish("", txnID, null, msg.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Evaluates pending lock requests after a lock is released,
     * granting locks if now possible.
     *
     * @param freedVector the vector ID whose lock was freed.
     * @param freedPos    the position within the vector that was freed.
     * @throws Exception if ZooKeeper or messaging fails.
     */
    private void checkPendingRequests(String freedVector, int freedPos) throws Exception {
        String pendingPath = basePath + "/" + freedVector + "/" + freedPos + "/" + waitingSuffix;
        List<String> pendingTxns = rnm.getPendingTransactions(pendingPath);

        for (String txnID : pendingTxns) {
            List<Lock> pendingLocks = rnm.getPendingRequest(txnID);

            if (pendingLocks == null) continue;

            if (canGrantAll(pendingLocks)) {
                holdLocks(txnID, pendingLocks);
                rnm.removePendingRequest(pendingPath, txnID);
                sendLockRequestMessage(txnID, "LOCK_GRANTED");
            }
        }
    }

    /**
     * Closes internal resources (RabbitMQ connection).
     *
     * @throws Exception if closure fails.
     */
    public void close() throws Exception {
        rabbitChannel.close();
    }

    @Override
    public void get_locks(String txnID, List<Lock> requestedLocks) {
        try {
            // check all RMs and collect any inactive ones
            List<String> inactiveRMs = new ArrayList<>();

            for (Lock lock : requestedLocks) {
                if (!rnm.checkRmStatus(lock.vectorId)) {
                    inactiveRMs.add(lock.vectorId);
                }
            }

            if (!inactiveRMs.isEmpty()) {
                LOGGER.warn("Inactive RMs detected for txnID={}: {}", txnID, String.join(", ", inactiveRMs));
                sendLockRequestMessage(txnID, "RM_INACTIVE: " + String.join(", ", inactiveRMs));
                return;
            }

            // check if all requested locks can be granted
            if (canGrantAll(requestedLocks)) {
                holdLocks(txnID, requestedLocks);
                sendLockRequestMessage(txnID, "LOCK_GRANTED");
            } else {
                rnm.addPendingRequest(pendingLocksPath, txnID, requestedLocks);
            }
        } catch (IOException ex) {
            LOGGER.error("Error sending lock request message for txnID={}", txnID, ex);
        } catch (Exception e) {
            LOGGER.error("Error during get_locks for txnID={}", txnID, e);
            try {
                sendLockRequestMessage(txnID, "LOCK_REQUEST_FAILED");
            } catch (IOException ex) {
                LOGGER.error("Error sending lock request message for txnID={}", txnID, ex);
            }
        }
    }


    @Override
    public void release_all_locks(String txnID) {
        try {
            List<Lock> heldLocks = rnm.getLocksHeld(locksHeldPath, txnID);

            if (heldLocks == null || heldLocks.isEmpty()) return;

            for (Lock lock : heldLocks) {
                rnm.clearHolder(basePath + "/" + lock.vectorId + "/" + lock.element + "/holder", lock.vectorId, lock.element); // Free the lock
                checkPendingRequests(lock.vectorId, lock.element);
            }

            rnm.clearLocksHeld(locksHeldPath, txnID); // Clean up txn record
        } catch (Exception e) {
            LOGGER.error("Error during release_all_locks for txnID={}", txnID, e);
        }
    }
}
