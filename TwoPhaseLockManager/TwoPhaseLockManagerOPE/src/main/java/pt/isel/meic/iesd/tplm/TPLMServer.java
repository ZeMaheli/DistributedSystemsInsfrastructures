package pt.isel.meic.iesd.tplm;

import jakarta.xml.ws.Endpoint;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TPLMServer {
    static final String HOSTNAME = "localhost";
    static final String PORT = "3000";

    static String rabbitMQHost ;
    static int rabbitMQPort;

    /**
     * Main method to start the TPLM client server.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        try {
            rabbitMQHost = args[0];
            rabbitMQPort = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.err.println("Usage: java TPLMServer <RabbitMQHost> <RabbitMQPort>");
            return;
        }

        TwoPhaseLockManager tplm = new TwoPhaseLockManager(rabbitMQHost, rabbitMQPort);

        // Publish client-only endpoint
        Endpoint clientEndpoint = Endpoint.create(tplm);
        clientEndpoint.publish("http://" + HOSTNAME + ":" + PORT + "/TPLMClient");

        // Publish transaction-manager-only endpoint
        Endpoint tmEndpoint = Endpoint.create(tplm);
        tmEndpoint.publish("http://" + HOSTNAME + ":" + (Integer.parseInt(PORT) + 1) + "/TPLMTM");
    }

}
