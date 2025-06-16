package pt.isel.meic.iesd.vs;

import jakarta.xml.ws.Endpoint;
import pt.isel.meic.iesd.tm.ExitCode;

import java.net.MalformedURLException;

public class SiteServer {
    static Integer ID;
    static final String HOSTNAME = "0.0.0.0"; 
    static final Integer PORT = 2060;

    static final String TM_HOSTNAME = "tm";
    static final Integer TM_PORT = 2059;

    public static void main(String[] args) {
        String hostname = HOSTNAME;
        int port = PORT;
        switch (args.length) {
            case 3:
                try {
                    port = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter PORT");
                    System.exit(ExitCode.INVALID_PORT.value());
                    return;
                }
            case 2:
                hostname = args[1];
            case 1:
                try {
                    ID = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter ID");
                    System.exit(ExitCode.INVALID_ID.value());
                    return;
                }
                break;
            default:
                System.err.println("No ID provided!");
                System.exit(ExitCode.INVALID_ID.value());
                return;
        }

        try {
            ResourceManager resourceManager = new ResourceManager(ID, hostname, port, TM_HOSTNAME, TM_PORT);
            Vector vector = new Vector(resourceManager);
            resourceManager.setVector(vector);
            Endpoint vectorEndpoint = Endpoint.create(vector);
            Endpoint xaEndpoint = Endpoint.create(new XaManager(resourceManager));
            System.out.println("Starting Vector Service...");
            vectorEndpoint.publish("http://" + hostname + ":" + port + "/Vector");
            System.out.println("Starting XA Communication...");
            xaEndpoint.publish("http://" + hostname + ":" + port + "/XA");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown detected! Setting ResourceManager offline...");
                resourceManager.setOffline();
            }));
        } catch (MalformedURLException e) {
            System.exit(ExitCode.INVALID_TM_URL.value());
            return;
        }
    }
}
