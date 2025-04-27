package pt.isel.meic.iesd.rnm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebService;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@WebService(endpointInterface = "pt.isel.meic.iesd.rnm.IReliableNodeManagerTPLM")
public class ReliableNodeManagerTPLM implements IReliableNodeManagerTPLM{
    private final ZooKeeper zooKeeper;
    private final String basePath;
    private final String locksHeldPath;
    private final String pendingLocksPath;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReliableNodeManagerTPLM(ZooKeeper zk) {
        this.zooKeeper = zk;
        this.basePath = "/locks";
        this.locksHeldPath = "/locks_held";
        this.pendingLocksPath = "/locks_pending";
    }

    @Override
    public boolean checkRmStatus(String vectorId) {
        try {
            String finalPath = "/resource-managers/" + "rm" + vectorId + "/status";

            byte[] data = zooKeeper.getData(finalPath, false, null);
            if (data != null) {
                String status = new String(data, StandardCharsets.UTF_8);
                return "online".equals(status);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getHolder(String path, String vectorId, int element) throws Exception {
        byte[] data = zooKeeper.getData(path, false, null);
        if (data != null) {
            return new String(data, StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public void setHolder(String path, String vectorId, int element, String txnID) throws Exception {
        ensurePathExists(path);
        zooKeeper.setData(path, txnID.getBytes(StandardCharsets.UTF_8), -1);
    }

    @Override
    public void addLockHeld(String txnID, Lock lock) throws Exception {
        String lockHeldPath = locksHeldPath + "/" + txnID;
        ensurePathExists(lockHeldPath);
        List<Lock> heldLocks = getLocksHeld(lockHeldPath, txnID); // Get the current list of locks

        // Add the new lock to the list
        heldLocks.add(lock);

        // Serialize the updated list back to JSON
        String lockInfo = objectMapper.writeValueAsString(heldLocks);

        // Set the data for this txnID path with the updated lock list
        zooKeeper.setData(lockHeldPath, lockInfo.getBytes(StandardCharsets.UTF_8), -1);
    }

    @Override
    public List<Lock> getLocksHeld(String path, String txnID) throws Exception {
        String locksHeldTxnPath = path + "/" + txnID;

        // Ensure the path exists (if the path does not exist, return an empty list)
        if (zooKeeper.exists(locksHeldTxnPath, false) == null) {
            return new ArrayList<>();
        }

        byte[] lockData = zooKeeper.getData(locksHeldTxnPath, false, null);
        if (lockData == null || lockData.length == 0) {
            return new ArrayList<>(); // Return empty list if no locks are stored
        }

        // Deserialize the JSON data into a list of Lock objects
        return objectMapper.readValue(lockData, objectMapper.getTypeFactory().constructCollectionType(List.class, Lock.class));
    }

    @Override
    public List<String> getPendingTransactions(String path) {
        try {
            if (zooKeeper.exists(path, false) == null) {
                // No pending transactions if path doesn't exist
                return emptyList();
            }

            byte[] data = zooKeeper.getData(path, false, null);

            if (data == null || data.length == 0) {
                return emptyList();
            }

            // Deserialize JSON array (e.g., ["txID", "txID1"]) into List<String>
            return objectMapper.readValue(data, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            System.out.println("Failed to retrieve pending transactions from path: " + path);
            return emptyList();
        }
    }

    @Override
    public List<Lock> getPendingRequest(String txnID) {
        List<Lock> locks = new ArrayList<>();

        // Check the /locks_pending/{txnID} path for the locks the txnID is requesting
        String pendingPath = basePath + "/locks_pending/" + txnID;

        try {
            byte[] data = zooKeeper.getData(pendingPath, false, null);
            if (data != null && data.length > 0) {
                List<Lock> pendingLocks = objectMapper.readValue(data, new TypeReference<List<Lock>>() {});
                locks.addAll(pendingLocks);
            }
        } catch (Exception e) {
            // If no data exists for this txnID, simply return an empty list (no pending request)
        }

        // Return the list of locks if found, or null if no locks are found
        return locks.isEmpty() ? null : locks;
    }

    @Override
    public void addPendingRequest(String path, String txnID, List<Lock> locks) throws Exception {
        String pendingPath = path + "/" + txnID;

        try {
            byte[] data = objectMapper.writeValueAsBytes(locks);
            ensurePathExists(pendingPath);
            zooKeeper.setData(pendingPath, data, -1);
        } catch (Exception e) {
            throw new Exception("Failed to add pending request for txnID=" + txnID, e);
        }
    }

    @Override
    public void removePendingRequest(String path, String txnID) throws Exception {
        String pendingPath = pendingLocksPath + "/" + txnID;

        try {
            if (zooKeeper.exists(path, false) != null) {
                // If the node exists, delete it to clear the lock holder
                zooKeeper.delete(path, -1);
            }
            // Check if the pending path exists
            if (zooKeeper.exists(pendingPath, false) != null) {
                // If it exists, delete the node for the given transaction ID
                zooKeeper.delete(pendingPath, -1);
            }
        } catch (Exception e) {
            throw new Exception("Failed to remove pending request for txnID=" + txnID, e);
        }
    }

    @Override
    public void clearHolder(String path, String vectorId, int element) throws Exception {
        try {
            // Check if the node exists before deleting it
            if (zooKeeper.exists(path, false) != null) {
                // If the node exists, delete it to clear the lock holder
                zooKeeper.delete(path, -1);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new Exception("Failed to clear lock holder for vectorId=" + vectorId + " and element=" + element, e);
        }
    }

    @Override
    public void clearLocksHeld(String path, String txnID) throws Exception {
        String locksHeldPath = path + "/" + txnID;

        try {
            // Check if the node exists for the txnID
            if (zooKeeper.exists(locksHeldPath, false) != null) {
                // Delete the txnID's lock record from locks_held
                zooKeeper.delete(locksHeldPath, -1);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new Exception("Failed to clear all locks held by txnID=" + txnID, e);
        }
    }

    private void ensurePathExists(String path) throws Exception {
        String[] nodes = path.split("/");

        StringBuilder currentPath = new StringBuilder();

        // Iterate through each node and create it if it doesn't exist
        for (String node : nodes) {
            if (!node.isEmpty()) {
                currentPath.append("/").append(node);

                if (zooKeeper.exists(currentPath.toString(), false) == null) {
                    zooKeeper.create(currentPath.toString(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }
        }
    }
}
