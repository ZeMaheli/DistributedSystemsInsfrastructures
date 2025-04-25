package pt.isel.meic.iesd.vs;

import jakarta.xml.ws.Endpoint;

public class SiteServer {
    static final String HOSTNAME = "0.0.0.0"; 
    static final Integer PORT = 2060;

    public static int main(String[] args) {
        String hostname = HOSTNAME;
        int port = PORT;
        switch (args.length) {
            case 2:
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter PORT");
                    return ExitCode.INVALID_PORT.value();
                }
            case 1:
                hostname = args[0];
        }

        Endpoint ep = Endpoint.create(new Vector());
        System.out.println("Starting VectorService...");
        ep.publish("http://" + hostname + ":" + port + "/Vector");
        return 0;
    }
}
