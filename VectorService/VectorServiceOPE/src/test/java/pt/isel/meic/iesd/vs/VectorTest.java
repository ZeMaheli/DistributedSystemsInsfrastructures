package pt.isel.meic.iesd.vs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */

public class VectorTest {

    private Vector vector;

    // Mock ResourceManager since it's needed by Vector
    static class MockResourceManager implements IResourceManager {
        public MockResourceManager() { }

        @Override
        public void register(int transactionID) {
            // No-op for testing
        }
    }

    @BeforeEach
    void setUp() {
        MockResourceManager resourceManager = new MockResourceManager();
        vector = new Vector(resourceManager);
    }

    @Test
    void testSingleRead() {
        int transactionId = 1;
        int value = vector.read(transactionId, 2); // Read position 2 (which is 56 initially)
        assertEquals(56, value);
    }

    @Test
    void testMultipleReads() {
        int transactionId = 1;
        int value1 = vector.read(transactionId, 0); // 300
        int value2 = vector.read(transactionId, 3); // 789
        assertEquals(300, value1);
        assertEquals(789, value2);
    }

    @Test
    void testSingleWrite() {
        int transactionId = 1;
        int newValue = 999;

        vector.write(transactionId, 1, newValue); // Update position 1 to 999
        int updatedValue = vector.read(transactionId, 1);

        assertEquals(newValue, updatedValue);
    }

    @Test
    void testMultipleWrites() {
        int transactionId = 1;

        vector.write(transactionId, 0, 111);
        vector.write(transactionId, 2, 222);
        vector.write(transactionId, 3, 333);

        int val0 = vector.read(transactionId, 0);
        int val2 = vector.read(transactionId, 2);
        int val3 = vector.read(transactionId, 3);

        assertEquals(111, val0);
        assertEquals(222, val2);
        assertEquals(333, val3);
    }
}
