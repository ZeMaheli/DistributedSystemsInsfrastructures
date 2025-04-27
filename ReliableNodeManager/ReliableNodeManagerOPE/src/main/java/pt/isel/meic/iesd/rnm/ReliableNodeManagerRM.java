package pt.isel.meic.iesd.rnm;

import jakarta.jws.WebService;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

@WebService(endpointInterface = "pt.isel.meic.iesd.rnm.IReliableNodeManagerRM")
public class ReliableNodeManagerRM implements IReliableNodeManagerRM{
    private final ZooKeeper zk;
    public ReliableNodeManagerRM(ZooKeeper zk) {
        this.zk = zk;
    }

    @Override
    public void registerInZookeeper(int ID, String HOSTNAME, int PORT) {
        try {
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
            createOrUpdateZnode(rmPath + "/vID", String.valueOf(ID));
            createOrUpdateZnode(rmPath + "/url", "http://" + HOSTNAME + ":" + PORT + "/Vector");

            System.out.println("Registered RM at path: " + rmPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void setOffline(int ID) {
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
