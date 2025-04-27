package pt.isel.meic.iesd.vs;

import jakarta.xml.ws.Endpoint;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class GenerateAxWSDL {

    static final String HOSTNAME = "0.0.0.0";
    static final Integer PORT = 2060;


    // Mock ResourceManager since it's needed by Vector
    static class MockResourceManager implements IResourceManager {
        private final Map<Integer, Map<Integer, Integer>> pendingWrites = new HashMap<>();

        @Override
        public void register(int transactionID) {
            // No-op for testing
        }

        @Override
        public void bufferWrite(int transactionID, int pos, int value) { }


        @Override
        public boolean prepare(int transactionID) {
            return false;
        }

        @Override
        public boolean commit(int transactionID) {
            return false;
        }

        @Override
        public boolean rollback(int transactionID) {
            return false;
        }

        @Override
        public Integer readBuffered(int transactionID, int pos) {
            return null;
        }

        @Override
        public void setOffline() { }
    }

    @Test
    void generateAxWSDL() throws InterruptedException {
        MockResourceManager resourceManager = new MockResourceManager();
        Endpoint xaEndpoint = Endpoint.create(new XaManager(resourceManager));
        System.out.println("Starting XA Communication...");
        xaEndpoint.publish("http://" + HOSTNAME + ":" + PORT + "/XA");

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
