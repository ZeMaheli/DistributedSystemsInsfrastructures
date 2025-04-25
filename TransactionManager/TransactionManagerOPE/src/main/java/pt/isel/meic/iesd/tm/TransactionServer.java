package pt.isel.meic.iesd.tm;

import jakarta.xml.ws.Endpoint;
import org.apache.zookeeper.ZooKeeper;

public class TransactionServer {
    static final String HOSTNAME = "0.0.0.0"; 
    static final Integer PORT = 2059;

    private static final String ZK_HOST = "0.0.0.0";
    private static final String QUEUE_NAME = "queue";
    private static ZooKeeper zk;

    public static void main(String[] args) {
        String hostname = HOSTNAME;
        int port = PORT;
        switch (args.length) {
            case 2:
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid parameter PORT");
                    System.exit(ExitCode.INVALID_PORT.value());
                }
            case 1:
                hostname = args[0];
        }

        TransactionRepository transactionRepository = new TransactionRepository();
        StateSynchronizer stateSynchronizer = new StateSynchronizer();
/*
        try {
            zk = new ZooKeeper(ZK_HOST, 3000, stateSynchronizer);
        } catch (IOException e) {
            System.err.println("Unable to connect to zookeeper");
            return ExitCode.INVALID_ZOOKEEPER_CONNECTION.value();
        }
*/
        Endpoint transactionEndpoint = Endpoint.create(new TransactionManager(transactionRepository));
        Endpoint axEndpoint = Endpoint.create(new AxManager(transactionRepository));
        System.out.println("Starting Transaction Manager...");
        transactionEndpoint.publish("http://" + hostname + ":" + port + "/Transaction");
        System.out.println("Starting AX Communication...");
        axEndpoint.publish("http://" + hostname + ":" + port + "/AX");
    }
}
