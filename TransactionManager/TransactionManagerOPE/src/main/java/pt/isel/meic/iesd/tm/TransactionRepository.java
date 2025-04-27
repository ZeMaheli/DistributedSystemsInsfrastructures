package pt.isel.meic.iesd.tm;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

public class TransactionRepository implements ITransactionRepository {

    private final ZooKeeper zk;
    private static final String BASE_PATH = "/tx";

    public TransactionRepository(String hostname) {
        try {
            zk = new ZooKeeper(hostname, 3000, null);
        } catch (IOException e) {
            System.err.println("Unable to connect to zookeeper");
            System.exit(ExitCode.INVALID_ZOOKEEPER_CONNECTION.value());
            throw new RuntimeException("Impossible state!");
        }
    }

    private void createNode(String path) throws InterruptedException, KeeperException {
        if (zk.exists(path, false) == null) {
            zk.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    private void updateNode(String path, String data) throws KeeperException, InterruptedException {
        // Check if the node exists
        if (zk.exists(path, false) == null) {
            // Create the node if it doesn't exist
            zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            // Update the node data if it exists
            zk.setData(path, data.getBytes(), -1);
        }
    }

    public void storeTransaction(Transaction transaction) {
        try {
            createNode(BASE_PATH);
            String txPath = BASE_PATH + "/" + transaction.getID();
            createNode(txPath);
            updateNode(txPath + "/state", transaction.getState().name());
            String rmPath = txPath + "/rm";
            createNode(rmPath);
            transaction.getResources().forEach( r -> {
                try {
                    System.out.println("Created rmID path");
                    updateNode(rmPath + "/" + r.id, r.hostname + ":" + r.port);
                } catch (InterruptedException | KeeperException e) {
                    System.exit(ExitCode.ZOOKEEPER_EXCEPTION.value());
                    return;
                }
            });

            System.out.println("Registered RM at path: " + txPath);
        } catch (InterruptedException | KeeperException e) {
            System.exit(ExitCode.ZOOKEEPER_EXCEPTION.value());
        }
    }

    private String getNode(String path) throws InterruptedException, KeeperException {
        byte[] data = zk.getData(path, false, null);
        return new String(data);
    }

    public Transaction getTransaction(int transactionID) {
        try {
            if (zk.exists(BASE_PATH, false) == null) return null;
            String txPath = BASE_PATH + "/" + transactionID;
            if (zk.exists(txPath, false) == null) return null;
            String state = getNode(txPath + "/state");
            Transaction transaction = new Transaction(transactionID);
            transaction.setState(TransactionState.valueOf(state));
            String rmPath = txPath + "/rm";
            // For each node inside rmPath
            // Register a resource in the transaction
            List<String> children = zk.getChildren(rmPath, false);
            for (String child : children) {
                String url = getNode(rmPath + "/" + child);
                Resource resource = new Resource();
                resource.setId(Integer.parseInt(child));
                String[] urlPieces = url.split(":");
                resource.setHostname(urlPieces[0]);
                resource.setPort(Integer.parseInt(urlPieces[1]));
                transaction.addResource(resource);
            }
            return transaction;
        } catch (InterruptedException | KeeperException e) {
            System.exit(ExitCode.ZOOKEEPER_EXCEPTION.value());
        }
        return null;
    }

}
