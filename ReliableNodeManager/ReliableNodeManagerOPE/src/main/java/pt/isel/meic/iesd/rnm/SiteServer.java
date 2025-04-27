package pt.isel.meic.iesd.rnm;

import jakarta.xml.ws.Endpoint;
import org.apache.zookeeper.ZooKeeper;

public class SiteServer {
    static final String HOSTNAME = "0.0.0.0";
    static final Integer PORTrnmtplm = 2050;
    static final Integer PORTrnmrm = 2051;
    static final Integer PORTrnmcl = 2052;

    public static void main(String[] args) {
        String hostname = HOSTNAME;
        int port1 = PORTrnmtplm;
        int port2 = PORTrnmrm;
        int port3 = PORTrnmcl;
        switch (args.length) {
            case 4:
                try {
                    port3 = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter PORT");
                    System.exit(1);
                    return;
                }
            case 3:
                try {
                    port2 = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter PORT");
                    System.exit(1);
                    return;
                }
            case 2:
                try {
                    port1 = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter PORT");
                    System.exit(1);
                    return;
                }
            case 1:
                hostname = args[0];
                break;
            case 0:
                break;
            default:
                return;
        }
        try {
            String zkHost = "0.0.0.0";
            ZooKeeper zooKeeper = new ZooKeeper(zkHost, 3000, null);

            ReliableNodeManagerTPLM rnmtplm = new ReliableNodeManagerTPLM(zooKeeper);
            ReliableNodeManagerRM rnmrm = new ReliableNodeManagerRM(zooKeeper);
            ReliableNodeManagerCl rnmcl = new ReliableNodeManagerCl(zooKeeper);

            // Create the endpoint and publish the service
            Endpoint rnmtplmEndpoint = Endpoint.create(rnmtplm);
            Endpoint rnmrmEndpoint = Endpoint.create(rnmrm);
            Endpoint rnmclEndpoint = Endpoint.create(rnmcl);
            System.out.println("Starting Reliable Node Manager...");
            rnmtplmEndpoint.publish("http://" + hostname + ":" + port1 + "/RNMTPLM");
            rnmrmEndpoint.publish("http://" + hostname + ":" + port2 + "/RNMRM");
            rnmclEndpoint.publish("http://" + hostname + ":" + port3 + "/RNMCL");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
