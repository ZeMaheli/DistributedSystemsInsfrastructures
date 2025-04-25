package pt.isel.meic.iesd.tplm;

import com.rabbitmq.client.*;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TPLM implements Watcher {
    private static final String ZK_HOST = "0.0.0.0";
    private static final String QUEUE_NAME = "queue";

    private ZooKeeper zk;
    private Connection rabbitConnection;
    private Channel rabbitChannel;

    public static void main(String[] args) throws Exception {
        new TPLM().start();
    }

    public void start() throws Exception {
        // Connect to ZooKeeper
        zk = new ZooKeeper(ZK_HOST, 3000, this);

        // Setup RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        rabbitConnection = factory.newConnection();
        rabbitChannel = rabbitConnection.createChannel();
        rabbitChannel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // Consume messages from clients
        rabbitChannel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

    // RabbitMQ Consumer
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        AMQP.BasicProperties props = delivery.getProperties();
        String replyTo = props.getReplyTo();
        String correlationId = props.getCorrelationId();

        // Assume JSON message with txnID and vector
        LockRequest request = LockRequest.fromJson(message);
        handleLockRequest(request, replyTo, correlationId);
    };

    // Lock logic
    private void handleLockRequest(LockRequest req, String replyTo, String correlationId) {
        String lockPath = "/locks/" + req.vector;
        String holderPath = lockPath + "/holder";
        String waitingPath = lockPath + "/waiting/" + req.txnID;

        try {
            Stat stat = zk.exists(holderPath, false);

            if (stat == null) {
                // Lock is free → grant it
                zk.create(holderPath, req.txnID.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                sendReply(replyTo, correlationId, req.txnID, req.vector, "lock-granted");
            } else {
                // Lock is held → queue the txn
                zk.create(waitingPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                sendReply(replyTo, correlationId, req.txnID, req.vector, "waiting");
            }
        } catch (KeeperException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendReply(String replyTo, String correlationId, String txnID, String vector, String status) throws IOException {
        String response = String.format("{\"txnID\":\"%s\",\"vector\":\"%s\",\"status\":\"%s\"}", txnID, vector, status);

        AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                .correlationId(correlationId)
                .build();

        rabbitChannel.basicPublish("", replyTo, replyProps, response.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public void process(WatchedEvent event) {
        // Handle ZooKeeper events if needed
        if (event.getType() == Event.EventType.NodeDeleted) {
            // Handle lock release or other events
        }
    }

    // Sample helper class to parse requests
    static class LockRequest {
        String txnID;
        String vector;

        public static LockRequest fromJson(String json) {
            // Simplified: use Jackson or Gson in real code
            String[] parts = json.replace("{", "").replace("}", "").replace("\"", "").split(",");
            LockRequest req = new LockRequest();
            for (String part : parts) {
                if (part.contains("txnID")) req.txnID = part.split(":")[1];
                if (part.contains("vector")) req.vector = part.split(":")[1];
            }
            return req;
        }
    }
}

