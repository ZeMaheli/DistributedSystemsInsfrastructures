package pt.isel.meic.iesd.vs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class XaManagerIntegrationTest {

    private MockResourceManager resourceManager;
    private Vector vector;
    private XaManager xaManager;


    // Mock ResourceManager since it's needed by Vector
    static class MockResourceManager implements IResourceManager {

        private Vector vector;
        private final Map<Integer, Map<Integer, Integer>> pendingWrites = new HashMap<>();

        @Override
        public void register(int transactionID) {
            // No-op for testing
        }

        public void setVector(Vector vector) {
            this.vector = vector;
        }

        @Override
        public void bufferWrite(int transactionID, int pos, int value) {
            pendingWrites.computeIfAbsent(transactionID, k -> new HashMap<>()).put(pos, value);
        }

        public boolean prepare(int transactionID) {
            // TODO - add validation logic - return FALSE if not ready, TRUE if ready
            System.out.println("Preparing transaction " + transactionID);

            if (!pendingWrites.containsKey(transactionID)) {
                System.out.println("Transaction " + transactionID + " not found in buffer! Voting NO.");
                return false;
            }

            return true;
        }

        public boolean commit(int transactionID) {
            System.out.println("Committing transaction " + transactionID);
            Map<Integer, Integer> writes = pendingWrites.remove(transactionID);
            if (writes != null) {
                for (Map.Entry<Integer, Integer> entry : writes.entrySet()) {
                    vector.applyWrite(entry.getKey(), entry.getValue());
                }
            }
            return true;
        }

        public boolean rollback(int transactionID) {
            System.out.println("Rolling back transaction " + transactionID);
            pendingWrites.remove(transactionID);
            return true;
        }

        @Override
        public Integer readBuffered(int transactionID, int pos) {
            if (pendingWrites.containsKey(transactionID) && pendingWrites.get(transactionID).containsKey(pos)) {
                return pendingWrites.get(transactionID).get(pos);
            }
            return null;
        }

        @Override
        public void setOffline() {

        }
    }

    @BeforeEach
    void setUp() {
        resourceManager = new MockResourceManager(); // use real ResourceManager
        vector = new Vector(resourceManager);
        resourceManager.setVector(vector);
        xaManager = new XaManager(resourceManager);
    }

    @Test
    void testCommitChangesVisible() {
        int transactionId = 1;

        // Read original value
        int original = vector.read(transactionId, 2); // index 2 -> initial value is 56
        assertEquals(56, original);

        // Write new value
        vector.write(transactionId, 2, 999);

        // Commit the transaction
        boolean commitSuccess = xaManager.commit(transactionId);
        assertTrue(commitSuccess);

        // Start new transaction and read again
        int transactionId2 = 2;
        int newValue = vector.read(transactionId2, 2);
        assertEquals(999, newValue, "Value should persist after commit");
    }

    @Test
    void testRollbackChangesNotVisible() {
        int transactionId = 3;

        // Read original value
        int original = vector.read(transactionId, 1); // index 1 -> initial value is 234
        assertEquals(234, original);

        // Write new value
        vector.write(transactionId, 1, 777);

        // Rollback the transaction
        boolean rollbackSuccess = xaManager.rollback(transactionId);
        assertTrue(rollbackSuccess);

        // Start new transaction and read again
        int transactionId4 = 4;
        int valueAfterRollback = vector.read(transactionId4, 1);
        assertEquals(234, valueAfterRollback, "Original value should be visible after rollback");
    }
}
