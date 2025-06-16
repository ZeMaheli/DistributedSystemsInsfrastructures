package pt.isel.meic.iesd.client;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import pt.isel.meic.iesd.rnm.IReliableNodeManagerCl;
import pt.isel.meic.iesd.rnm.ReliableNodeManagerClService;
import pt.isel.meic.iesd.tplm.ITwoPhaseLockManager;
import pt.isel.meic.iesd.tplm.TwoPhaseLockManagerService;
import pt.isel.meic.iesd.vs.VectorService;
import pt.isel.meic.iesd.vs.IVector;
import pt.isel.meic.iesd.tm.ITransaction;
import pt.isel.meic.iesd.tm.TransactionManagerService;

import javax.xml.namespace.QName;

public class VectorClient {
    private static final String RABBITMQ_HOST = "rabbitmq";
    private Channel rabbitChannel;
    private Connection rabbitConnection;

    private ITransaction tmPort;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VectorClient cl = new VectorClient();
        cl.setup();

        if (args.length >= 5) {
            // Run a single transaction from args
            String v1 = args[0];
            int pos1 = Integer.parseInt(args[1]);
            String v2 = args[2];
            int pos2 = Integer.parseInt(args[3]);
            int amount = Integer.parseInt(args[4]);
            cl.executeTransaction(v1, pos1, v2, pos2, amount);
            cl.close();
            return;
        }

        System.out.println("Enter transactions in format: <v1ID> <pos1> <v2ID> <pos2> <amount>");
        System.out.println("Type 'exit' to quit");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) break;

            String[] tokens = line.split("\\s+");
            if (tokens.length < 5) {
                System.out.println("Invalid input. Please provide 5 values.");
                continue;
            }

            try {
                String v1 = tokens[0];
                int pos1 = Integer.parseInt(tokens[1]);
                String v2 = tokens[2];
                int pos2 = Integer.parseInt(tokens[3]);
                int amount = Integer.parseInt(tokens[4]);

                cl.executeTransaction(v1, pos1, v2, pos2, amount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format.");
            }
        }

        cl.close();
    }

    public boolean executeTransaction(String v1, int pos1, String v2, int pos2, int amount) {
        int txnID = -1;
        try {
            txnID = tmPort.begin();
            sendLocksRequest(txnID, v1, pos1, v2, pos2);
            // Block until message arrives
            listenForLockReply(txnID);

            // Do read/write on vectors
            vectorOperations(txnID, v1, pos1, v2, pos2, amount);

            String reply = tmPort.commitTransaction(txnID);
            System.out.println("Committed txn " + txnID + ": " + reply);
            return true;
        } catch (Exception e) {
            System.out.println("Transaction failed: " + e.getMessage());
            if (txnID != -1) {
                try {
                    tmPort.rollbackTransaction(txnID);
                } catch (Exception rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            return false;
        }
    }

    public void setup() {
        try {
            TransactionManagerService tm = new TransactionManagerService();
            tmPort = tm.getTransactionManagerPort();
            // Setup RabbitMQ
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RABBITMQ_HOST);
            factory.setPort(5672);
            rabbitConnection = factory.newConnection();
            rabbitChannel = rabbitConnection.createChannel();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    public void close() {
        try {
            if (rabbitChannel != null && rabbitChannel.isOpen()) {
                rabbitChannel.close();
            }
            if (rabbitConnection != null && rabbitConnection.isOpen()) {
                rabbitConnection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLocksRequest(int txnID, String v1, int pos1, String v2, int pos2) {
        TwoPhaseLockManagerService tplmService = new TwoPhaseLockManagerService();
        ITwoPhaseLockManager tplm = tplmService.getTwoPhaseLockManagerPort();
        tplm.getLocks(String.valueOf(txnID), v1, pos1, v2, pos2);
    }

    public void listenForLockReply(int txnID) throws Exception {
        rabbitChannel.queueDeclare(String.valueOf(txnID), false, false, false, null);

        System.out.println("Waiting for lock reply for transaction " + txnID + "...");

        CountDownLatch latch = new CountDownLatch(1);

        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received lock reply: " + message);
            latch.countDown();
        };

        rabbitChannel.basicConsume(String.valueOf(txnID), true, callback, consumerTag -> {});
        latch.await(); // block until response
    }

    private void vectorOperations(int txnID, String v1, int pos1, String v2, int pos2, int amount) throws Exception{
        IVector port1 = getVectorServicePort(v1);
        IVector port2 = getVectorServicePort(v2);
        int value = port1.read(txnID, pos1);
        port1.write(txnID, pos1, value - amount);

        int value2 = port2.read(txnID, pos2);
        port2.write(txnID, pos2, value2 + amount);
    }

    private IVector getVectorServicePort(String vectorID) throws Exception {
        String url = getVectorServiceUrl(vectorID);
        URL wsdlUrl = new URL(url + "?wsdl");
        QName serviceName = new QName("http://vs.iesd.meic.isel.pt/", "VectorService");
        VectorService vectorService = new VectorService(wsdlUrl, serviceName);
        return vectorService.getVectorPort();
    }

    public String getVectorServiceUrl(String vectorID) {
        ReliableNodeManagerClService rnmService = new ReliableNodeManagerClService();
        IReliableNodeManagerCl rnm = rnmService.getReliableNodeManagerClPort();
        return rnm.getVectorServiceUrl(vectorID);
    }
}