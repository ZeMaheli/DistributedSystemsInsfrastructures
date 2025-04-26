package pt.isel.meic.iesd.vs;

import jakarta.xml.ws.Service;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import pt.isel.meic.iesd.tm.AxManagerService;
import pt.isel.meic.iesd.tm.IAX;
import pt.isel.meic.iesd.tm.Resource;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager implements IResourceManager {

    private final int ID;
    private final String HOSTNAME;
    private final int PORT;
    static IAX axManager;
    private Vector vector;
    private final Map<Integer, Map<Integer, Integer>> pendingWrites = new HashMap<>();
    private static final String ZK_HOST = "0.0.0.0";
    private ZooKeeper zk;

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public ResourceManager(int id, String hostname, int port, String tm_hostname, int tm_port) throws MalformedURLException {
        this.ID = id;
        this.HOSTNAME = hostname;
        this.PORT = port;
        URL wsdlUrl = new URL("http://" + tm_hostname + ":" + tm_port + "/AX?wsdl");
        QName serviceName = new QName("http://example.com/soap", "MyService");
        Service service = AxManagerService.create(wsdlUrl, serviceName);
        axManager = service.getPort(IAX.class);
        registerInZookeeper();
    }

    public void register(int transactionID) {
        Resource resource = new Resource();
        resource.setId(ID);
        resource.setHostname(HOSTNAME);
        resource.setPort(PORT);
        axManager.register(transactionID, resource);
    }

    public void bufferWrite(int transactionID, int pos, int value) {
        pendingWrites.computeIfAbsent(transactionID, k -> new HashMap<>()).put(pos, value);
    }

    public boolean prepare(int transactionID) {
        // TODO - add validation logic - return FALSE if not ready, TRUE if ready
        System.out.println("Preparing transaction " + transactionID);

        if (!pendingWrites.containsKey(transactionID)) {
            System.out.println("Transaction " + transactionID + " not found in buffer! Voting NO.");
            return false;
        }

        return true;
    }

    public boolean commit(int transactionID) {
        System.out.println("Committing transaction " + transactionID);
        Map<Integer, Integer> writes = pendingWrites.remove(transactionID);
        if (writes != null) {
            for (Map.Entry<Integer, Integer> entry : writes.entrySet()) {
                vector.applyWrite(entry.getKey(), entry.getValue());
            }
        }
        return true;
    }

    public boolean rollback(int transactionID) {
        System.out.println("Rolling back transaction " + transactionID);
        pendingWrites.remove(transactionID);
        return true;
    }

    public Integer readBuffered(int transactionID, int pos) {
        if (pendingWrites.containsKey(transactionID) && pendingWrites.get(transactionID).containsKey(pos)) {
            return pendingWrites.get(transactionID).get(pos);
        }
        return null;
    }

    private void registerInZookeeper() {
        try {
            zk = new ZooKeeper(ZK_HOST, 3000, event -> {});

            // Ensure the parent node exists
            if (zk.exists("/resource-managers", false) == null) {
                zk.create("/resource-managers", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // Create the path for RM node
            String rmPath = "/resource-managers/rm" + ID;

            if (zk.exists(rmPath, false) == null) {
                zk.create(rmPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            // Create or update the status, vID, and url in separate znodes
            createOrUpdateZnode(rmPath + "/status", "online");
            createOrUpdateZnode(rmPath + "/vID", "vec" + ID);
            createOrUpdateZnode(rmPath + "/url", "http://" + HOSTNAME + ":" + PORT + "/Vector");

            System.out.println("Registered RM at path: " + rmPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setOffline() {
        try {
            String statusPath = "/resource-managers/rm" + ID + "/status";

            // Check if the 'status' znode exists
            if (zk.exists(statusPath, false) != null) {
                // Set the status znode to "offline"
                zk.setData(statusPath, "offline".getBytes(), -1);

                System.out.println("Set RM " + ID + " as offline in Zookeeper.");
            } else {
                System.err.println("Status znode for RM " + ID + " not found.");
            }
            zk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createOrUpdateZnode(String path, String data) throws KeeperException, InterruptedException {
        // Check if the znode exists
        if (zk.exists(path, false) == null) {
            // Create the znode if it doesn't exist
            zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            // Update the znode data if it exists
            zk.setData(path, data.getBytes(), -1);
        }
    }
}
