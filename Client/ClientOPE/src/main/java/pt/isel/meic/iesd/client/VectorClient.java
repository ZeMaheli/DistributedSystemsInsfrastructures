package pt.isel.meic.iesd.client;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import pt.isel.meic.iesd.vs.VectorService;
import pt.isel.meic.iesd.vs.IVector;
import pt.isel.meic.iesd.tm.ITransaction;
import pt.isel.meic.iesd.tm.TransactionManagerService;

import javax.xml.namespace.QName;

public class VectorClient {
    private static final String RABBITMQ_HOST = "localhost";
    private Channel rabbitChannel;
    private ZooKeeper zk;
    private static final String ZK_HOST = "0.0.0.0";

    public static void main(String[] args) {
        if (args.length < 5) {
            System.err.println("Usage: VectorClient <v1ID> <pos1> <v2ID> <pos2> <amount>");
            System.exit(1);
        }
        String v1 = args[0];
        int pos1 = Integer.parseInt(args[1]);
        String v2 = args[2];
        int pos2 = Integer.parseInt(args[3]);
        int amount = Integer.parseInt(args[4]);

        VectorClient cl = new VectorClient();
        cl.setup();

        TransactionManagerService tm = new TransactionManagerService();
        ITransaction tmPort = tm.getTransactionManagerPort();

        int txnID = tmPort.begin();

        try {
            cl.sendLocksRequest(txnID, v1, pos1, v2, pos2);
            // Block until message arrives
            cl.listenForLockReply(txnID);

            // Do read/write on vectors
            cl.vectorOperations(txnID, v1, pos1, v2, pos2, amount);

            String reply = tmPort.commit(txnID);
            System.out.println(reply);
        } catch (Exception e) {
            tmPort.rollback(txnID);
        }
    }

    private void setup() {
        try {
            // Setup RabbitMQ
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RABBITMQ_HOST);
            Connection rabbitConnection = factory.newConnection();
            rabbitChannel = rabbitConnection.createChannel();
            // Setup zookeeper
            zk = new ZooKeeper(ZK_HOST, 3000, event -> {});
        } catch (Exception e) {
            System.exit(1);
        }
    }

    private void sendLocksRequest(int txnID, String v1, int pos1, String v2, int pos2) {
        // TODO - Update this
        TPLM tplm = new TPLM();
        //IVector port = service.getVectorPort();
        Lock lock1 = new Lock("vec" + v1, pos1);
        Lock lock2 = new Lock("vec" + v2, pos2);
        port.get_locks(txnID, List.of(lock1, lock2));
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

    public String getVectorServiceUrl(String vectorID) throws KeeperException, InterruptedException {
        String rmPath = "/resource-managers/rm" + vectorID;

        // Fetch the 'url' znode for the given vectorID
        String urlPath = rmPath + "/url";
        Stat stat = new Stat();
        byte[] data = zk.getData(urlPath, false, stat);

        if (data != null) {
            // Data is a simple string with the URL
            return new String(data, StandardCharsets.UTF_8);
        } else {
            throw new IllegalArgumentException("No URL found for vectorID: " + vectorID);
        }
    }
}