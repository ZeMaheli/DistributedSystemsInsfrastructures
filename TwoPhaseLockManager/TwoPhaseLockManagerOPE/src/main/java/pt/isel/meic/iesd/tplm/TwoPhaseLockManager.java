package pt.isel.meic.iesd.tplm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.meic.iesd.rnm.IReliableNodeManagerTPLM;
import pt.isel.meic.iesd.rnm.Lock;
import pt.isel.meic.iesd.rnm.ReliableNodeManagerTPLMService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    private final IReliableNodeManagerTPLM rnm;
    private final Channel rabbitChannel;

    /**
     * Constructs a TwoPhaseLockManager instance, setting up configuration and RabbitMQ connection.
     *
     * @param rabbitMQHost the RabbitMQ server address for lock grant notifications.
     * @param rabbitMQPort the RabbitMQ server port.
     */
    public TwoPhaseLockManager(String rabbitMQHost, int rabbitMQPort) {
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
     */
    private Channel setupRabbiMQChannel(String rabbitMQHost, int rabbitMQPort) {
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
     * Checks if all requested locks are currently available (not held).
     *
     * @param vectorPositions list of locks requested.
     * @return true if all locks can be granted, false otherwise.
     * @throws Exception if ZooKeeper access fails.
     */
    private boolean canGrantAll(List<Lock> vectorPositions) throws Exception {
        for (Lock lock : vectorPositions) {
            String holder = rnm.getHolder(lock.vectorId, lock.element);
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
            rnm.setHolder(lock.vectorId, lock.element, txnID);
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
        List<String> pendingTxns = rnm.getPendingTransactions(freedVector, freedPos);

        if (pendingTxns == null || pendingTxns.isEmpty()) {
            return;
        }

        String nextTnxID = pendingTxns.get(0);
        List<Lock> pendingLocks = rnm.getPendingRequest(nextTnxID);

        if (pendingLocks == null) {
            LOGGER.warn("No pending locks found for txnID={}", nextTnxID);
            return;
        }

        if (canGrantAll(pendingLocks)) {
            holdLocks(nextTnxID, pendingLocks);
            rnm.removePendingRequest(pendingLocks, nextTnxID);
            sendLockRequestMessage(nextTnxID, "LOCK_GRANTED");
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
    public void get_locks(String txnID, String v1, int pos1, String v2, int pos2) {
        try {
            List<Lock> requestedLocks = List.of(new Lock(v1, pos1), new Lock(v2, pos2));
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
                System.out.println("Can grant all locks");
                holdLocks(txnID, requestedLocks);
                sendLockRequestMessage(txnID, "LOCK_GRANTED");
            } else {
                System.out.println("Can't grant all locks, add to waiting list");
                rnm.addPendingRequest(txnID, requestedLocks);
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
        System.out.println("Releasing all locks for txn: " + txnID);
        try {
            List<Lock> heldLocks = rnm.getLocksHeld(txnID);

            if (heldLocks == null || heldLocks.isEmpty()) return;

            for (Lock lock : heldLocks) {
                System.out.println("Releasing lock: " + lock.vectorId + " " + lock.element);
                rnm.clearHolder(lock.vectorId, lock.element); // Free the lock
                checkPendingRequests(lock.vectorId, lock.element);
            }
            rnm.clearLocksHeld(txnID); // Clean up txn record
        } catch (Exception e) {
            LOGGER.error("Error during release_all_locks for txnID={}", txnID, e);
        }
    }
}
