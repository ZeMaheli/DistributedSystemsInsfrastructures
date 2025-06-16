package pt.isel.meic.iesd.tm;

import jakarta.xml.ws.Endpoint;

public class TransactionServer {
    private static  final String HOSTNAME = "0.0.0.0";
    private static final Integer PORT = 2059;

    private static final String ZK_HOST = System.getenv("ZOOKEEPER_HOST");

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

        TransactionRepository transactionRepository = new TransactionRepository(ZK_HOST);
        TransactionService transactionService = new TransactionService(transactionRepository);
        XaRepository xaRepository = new XaRepository();

        Endpoint transactionEndpoint = Endpoint.create(new TransactionManager(transactionService, xaRepository));
        Endpoint axEndpoint = Endpoint.create(new AxManager(transactionService));
        System.out.println("Starting Transaction Manager...");
        transactionEndpoint.publish("http://" + hostname + ":" + port + "/Transaction");
        System.out.println("Starting AX Communication...");
        axEndpoint.publish("http://" + hostname + ":" + port + "/AX");
    }
}
