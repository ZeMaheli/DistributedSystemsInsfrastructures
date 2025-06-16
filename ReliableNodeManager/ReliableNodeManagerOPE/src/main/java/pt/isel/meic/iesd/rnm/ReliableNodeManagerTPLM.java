package pt.isel.meic.iesd.rnm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebService;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;

@WebService(endpointInterface = "pt.isel.meic.iesd.rnm.IReliableNodeManagerTPLM")
public class ReliableNodeManagerTPLM implements IReliableNodeManagerTPLM{
    private final ZooKeeper zooKeeper;
    private String basePath;
    private String locksHeldPath;
    private String pendingLocksPath;
    private String holderSuffix;
    private String waitingSuffix;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReliableNodeManagerTPLM(ZooKeeper zk) {
        this.zooKeeper = zk;
        loadConfig();
    }

    /**
     * Loads Zookeeper configuration properties from the `zookeeper.properties` file.
     *
     * @throws RuntimeException if the configuration cannot be loaded.
     */
    private void loadConfig() {
        System.out.println("Loading zookeeper configuration...");
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("zookeeper.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find zookeeper.properties");
            }
            prop.load(input);
            basePath = prop.getProperty("locks.base_path");
            holderSuffix = prop.getProperty("locks.holder_suffix");
            waitingSuffix = prop.getProperty("locks.waiting_suffix");
            pendingLocksPath = prop.getProperty("locks.pending_base_path");
            locksHeldPath = prop.getProperty("locks.held_base_path");
        } catch (Exception ex) {
            System.out.println("Error loading zookeeper configuration");
        }
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
    public String getHolder(String vectorId, int element) throws Exception {
        String path = basePath + "/" + vectorId + "/" + element + holderSuffix;
        try {
            byte[] data = zooKeeper.getData(path, false, null);
            if (data != null) {
                return new String(data, StandardCharsets.UTF_8);
            }
            return null;
        } catch (KeeperException.NoNodeException e) {
            return null;
        }
    }

    @Override
    public void setHolder(String vectorId, int element, String txnID) throws Exception {
        String holderPath = basePath + "/" + vectorId + "/" + element + holderSuffix;
        ensurePathExists(holderPath);
        zooKeeper.setData(holderPath, txnID.getBytes(StandardCharsets.UTF_8), -1);
    }

    @Override
    public void addLockHeld(String txnID, Lock lock) throws Exception {
        String lockHeldPath = locksHeldPath + "/" + txnID;
        ensurePathExists(lockHeldPath);
        List<Lock> heldLocks = getLocksHeld(txnID); // Get the current list of locks

        // Add the new lock to the list
        heldLocks.add(lock);

        // Serialize the updated list back to JSON
        String lockInfo = objectMapper.writeValueAsString(heldLocks);

        // Set the data for this txnID path with the updated lock list
        zooKeeper.setData(lockHeldPath, lockInfo.getBytes(StandardCharsets.UTF_8), -1);
    }

    @Override
    public List<Lock> getLocksHeld(String txnID) throws Exception {
        String locksHeldTxnPath = locksHeldPath + "/" + txnID;

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
    public List<String> getPendingTransactions(String vectorId, int element) {
        String pendingPath = basePath + "/" + vectorId + "/" + element + waitingSuffix;
        try {
            if (zooKeeper.exists(pendingPath, false) == null) {
                // No pending transactions if path doesn't exist
                return emptyList();
            }

            byte[] data = zooKeeper.getData(pendingPath, false, null);

            if (data == null || data.length == 0) {
                return emptyList();
            }

            // Deserialize JSON array (e.g., ["txID", "txID1"]) into List<String>
            return objectMapper.readValue(data, new TypeReference<>() {});
        } catch (Exception e) {
            System.out.println("Failed to retrieve pending transactions from path: " + pendingPath);
            return emptyList();
        }
    }

    @Override
    public List<Lock> getPendingRequest(String txnID) {
        List<Lock> locks = new ArrayList<>();

        // Check the /locks_pending/{txnID} path for the locks the txnID is requesting
        String pendingPath = pendingLocksPath + "/" + txnID;

        try {
            byte[] data = zooKeeper.getData(pendingPath, false, null);
            if (data != null && data.length > 0) {
                List<Lock> pendingLocks = objectMapper.readValue(data, new TypeReference<>() {
                });
                locks.addAll(pendingLocks);
            }
        } catch (Exception e) {
            // If no data exists for this txnID, simply return an empty list (no pending request)
        }

        // Return the list of locks if found, or null if no locks are found
        return locks.isEmpty() ? null : locks;
    }

    @Override
    public void addPendingRequest(String txnID, List<Lock> locks) throws Exception {
        try {
            writeToPendingLocksPath(txnID, locks);
            writeToIndividualWaitingPaths(txnID, locks);
        } catch (Exception e) {
            throw new Exception("Failed to add pending request for txnID=" + txnID, e);
        }
    }

    private void writeToPendingLocksPath(String txnID, List<Lock> locks) throws Exception {
        String pendingPath = pendingLocksPath + "/" + txnID;

        byte[] data = objectMapper.writeValueAsBytes(locks);
        ensurePathExists(pendingPath);
        zooKeeper.setData(pendingPath, data, -1);
    }

    private void writeToIndividualWaitingPaths(String txnID, List<Lock> locks) throws Exception {
        for (Lock lock : locks) {
            String waitingPath = basePath + "/" + lock.vectorId + "/" + lock.element + waitingSuffix;
            List<String> txnList = new ArrayList<>();

            if (zooKeeper.exists(waitingPath, false) != null) {
                byte[] waitingData = zooKeeper.getData(waitingPath, false, null);
                if (waitingData != null && waitingData.length > 0) {
                    txnList = objectMapper.readValue(waitingData, new TypeReference<>() {});
                }
            } else {
                ensurePathExists(waitingPath);
            }

            if (!txnList.contains(txnID)) {
                txnList.add(txnID);
                byte[] updatedData = objectMapper.writeValueAsBytes(txnList);
                zooKeeper.setData(waitingPath, updatedData, -1);
            }
        }
    }

    @Override
    public void removePendingRequest(List<Lock> locks, String txnID) throws Exception {
        String pendingPath = pendingLocksPath + "/" + txnID;

        try {
            for (Lock lock: locks) {
                String waitingPath = basePath + "/" + lock.vectorId + "/" + lock.element + waitingSuffix;

                // Remove txnID from the waiting list
                if (zooKeeper.exists(waitingPath, false) != null) {
                    byte[] data = zooKeeper.getData(waitingPath, false, null);
                    List<String> txnList = new ArrayList<>();
                    if (data != null && data.length > 0) {
                        txnList = objectMapper.readValue(data, new TypeReference<>() {});
                    }
                    txnList.removeIf(t -> t.equals(txnID));

                    if (txnList.isEmpty()) {
                        zooKeeper.delete(waitingPath, -1);
                    } else {
                        byte[] updatedData = objectMapper.writeValueAsBytes(txnList);
                        zooKeeper.setData(waitingPath, updatedData, -1);
                    }
                }
            }

            // Remove txnID from locks_pending
            if (zooKeeper.exists(pendingPath, false) != null) {
                zooKeeper.delete(pendingPath, -1);
            }
        } catch (Exception e) {
            throw new Exception("Failed to remove pending request for txnID=" + txnID, e);
        }
    }

    @Override
    public void clearHolder(String vectorId, int element) throws Exception {
        String path = basePath + "/" + vectorId + "/" + element + holderSuffix;
        System.out.println("Clearing holder for path: " + path);
        try {
            // Check if the node exists before deleting it
            if (zooKeeper.exists(path, false) != null) {
                // If the node exists, delete it to clear the lock holder
                System.out.println("Cleared holder for path: " + path);
                zooKeeper.delete(path, -1);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new Exception("Failed to clear lock holder for vectorId=" + vectorId + " and element=" + element, e);
        }
    }

    @Override
    public void clearLocksHeld(String txnID) throws Exception {
        String path = locksHeldPath + "/" + txnID;
        System.out.println("Clearing locks held for path: " + path);
        try {
            // Check if the node exists for the txnID
            if (zooKeeper.exists(path, false) != null) {
                System.out.println("Cleared locks held for path: " + path);
                // Delete the txnID's lock record from locks_held
                zooKeeper.delete(path, -1);
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
