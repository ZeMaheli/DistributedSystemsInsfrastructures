package pt.isel.meic.iesd.rnm;

import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.isel.meic.iesd.rnm.tplm.ReliableNodeManagerTPLM;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReliableNodeManagerTPLMTest {

    private static TestingServer zkServer;
    private static ZooKeeper zkClient;
    private static ReliableNodeManagerTPLM rnm;
    private final String v1 = "1";

    private final String v2 = "2";
    private final String txnID = "1";
    private final int elem = 3;

    private final int elem2 = 0;

    @BeforeAll
    static void setUp() throws Exception {
        // Start embedded ZooKeeper server
        zkServer = new TestingServer();
        zkServer.start();

        // Connect ZooKeeper client
        zkClient = new ZooKeeper(zkServer.getConnectString(), 3000, event -> {});

        // Create RNM instance with embedded ZooKeeper
        rnm = new ReliableNodeManagerTPLM(zkClient);
    }

    @AfterAll
    static void tearDown() throws Exception {
        zkClient.close();
        zkServer.close();
    }

    @Test
    void testSetHolderGetHolderAndClearHolder() throws Exception {
        rnm.setHolder(v1, elem, txnID);
        String txnID2 = "2";
        rnm.setHolder(v2, elem2, txnID2);
        String holder = rnm.getHolder(v1, elem);
        assertEquals(txnID, holder);
        rnm.clearHolder(v1, elem);
        assertNull(rnm.getHolder(v1, elem));
        String holder2 = rnm.getHolder(v2, elem2);
        assertEquals(txnID2, holder2);
        rnm.clearHolder(v2, elem2);
        assertNull(rnm.getHolder(v2, elem2));
    }

    @Test
    void testAddAndGetLocksHeld() throws Exception {
        Lock lock = new Lock(v1, elem);

        rnm.addLockHeld(txnID, lock);
        List<Lock> heldLocks = rnm.getLocksHeld(txnID);

        assertNotNull(heldLocks);
        assertEquals(1, heldLocks.size());

        Lock lock2 = new Lock(v2, elem2);
        rnm.addLockHeld(txnID, lock2);
        heldLocks = rnm.getLocksHeld(txnID);

        assertEquals(2, heldLocks.size());
        assertEquals(lock.vectorId, heldLocks.get(0).vectorId);
        assertEquals(lock.element, heldLocks.get(0).element);
        assertEquals(lock2.vectorId, heldLocks.get(1).vectorId);
        assertEquals(lock2.element, heldLocks.get(1).element);
    }

    @Test
    void testAddAndRemovePendingRequest() throws Exception {
        Lock lock = new Lock(v1, elem);
        Lock lock2 = new Lock(v2, elem2);
        List<Lock> locks = List.of(lock, lock2);

        rnm.addPendingRequest(txnID, locks);

        List<Lock> result = rnm.getPendingRequest(txnID);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(lock.vectorId, result.get(0).vectorId);
        assertEquals(lock.element, result.get(0).element);
        assertEquals(lock2.vectorId, result.get(1).vectorId);
        assertEquals(lock2.element, result.get(1).element);

        rnm.removePendingRequest(locks, txnID);
        List<Lock> result2 = rnm.getPendingRequest(txnID);
        assertNull(result2);
    }

}