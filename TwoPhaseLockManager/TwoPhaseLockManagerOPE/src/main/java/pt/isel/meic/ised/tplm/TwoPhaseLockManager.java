package pt.isel.meic.ised.tplm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pt.isel.meic.iesd.rnm.IReliableNodeManagerTPLM;
import pt.isel.meic.iesd.rnm.ReliableNodeManagerTPLMService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import pt.isel.meic.iesd.rnm.Lock;

/**
 * TwoPhaseLockManager (TPLM) manages locks across distributed Resource Managers (RMs),
 * ensuring transactional access to vector elements using a two-phase locking protocol.
 * <p>
 * It verifies RM availability, grants locks, queues pending requests, and
 * communicates lock grants asynchronously through RabbitMQ.
 */
public class TwoPhaseLockManager implements ITwoPhaseLockManager {

    protected final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(TwoPhaseLockManager.class.getName());

    String basePath;
    String holderSuffix;
    String waitingSuffix;
    String pendingLocksPath;
    String locksHeldPath;

    private final IReliableNodeManagerTPLM rnm;
    private final Channel rabbitChannel;
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Constructs a TwoPhaseLockManager instance, setting up configuration and RabbitMQ connection.
     *
     * @param rabbitmqHost the RabbitMQ server address for lock grant notifications.
     * @throws Exception if setup fails.
     */
    public TwoPhaseLockManager(String rabbitmqHost) throws Exception {
        try {
            loadConfig();

            // Setup RabbitMQ
            LOG.info("Starting RabbitMQ Client...");
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitmqHost);
            Connection connection = factory.newConnection();
            rabbitChannel = connection.createChannel();
            LOG.info("RabbitMQ session established.");

            ReliableNodeManagerTPLMService rnmService = new ReliableNodeManagerTPLMService();
            rnm = rnmService.getReliableNodeManagerTPLMPort();
        } catch (Exception e) {
            LOG.log(System.Logger.Level.ERROR, "TwoPhaseLockManager instantiation error occurred with:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads TPLM configuration properties from the `tplm.properties` file.
     *
     * @throws RuntimeException if the configuration cannot be loaded.
     */
    private void loadConfig() {
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
            throw new RuntimeException("Failed to load configuration", ex);
        }
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
            rnm.addLockHeld(holderPath, lock); // Track locks held by txn
        }
    }

    /**
     * Publishes a "LOCK_GRANTED" message to the RabbitMQ queue named after the transaction ID.
     *
     * @param txnID the transaction receiving the lock grant notification.
     * @throws Exception if RabbitMQ publishing fails.
     */
    private void sendLockGrantedMessage(String txnID) throws Exception {
        String message = "LOCK_GRANTED";
        rabbitChannel.queueDeclare(txnID, false, false, false, null);
        rabbitChannel.basicPublish("", txnID, null, message.getBytes(StandardCharsets.UTF_8));
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
                sendLockGrantedMessage(txnID);
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
    public int get_locks(String txnID, List<Lock> requestedLocks) {
        try {
            // check all RMs and collect any inactive ones
            List<String> inactiveRMs = new ArrayList<>();

            for (Lock lock : requestedLocks) {
                if (!rnm.checkRmStatus(lock.vectorId)) {
                    inactiveRMs.add(lock.vectorId);
                }
            }

            if (!inactiveRMs.isEmpty()) {
                LOG.warning("Inactive RMs detected for txnID=" + txnID + ": " + String.join(", ", inactiveRMs));
                return -1;
            }

            // check if all requested locks can be granted
            if (canGrantAll(requestedLocks)) {
                holdLocks(txnID, requestedLocks);
                sendLockGrantedMessage(txnID);
            } else {
                rnm.addPendingRequest(pendingLocksPath, txnID, requestedLocks);
            }
            return 0;
        } catch (Exception e) {
            LOG.log(System.Logger.Level.ERROR, "Error during get_locks for txnID=" + txnID, e);
            return -1;
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
            LOG.log(System.Logger.Level.ERROR, "Error during release_all_locks for txnID=" + txnID, e);
        }
    }
}
