package pt.isel.meic.iesd.rnm.cl;

import jakarta.jws.WebService;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import pt.isel.meic.iesd.rnm.IReliableNodeManagerCl;

import java.nio.charset.StandardCharsets;

@WebService(endpointInterface = "pt.isel.meic.iesd.rnm.IReliableNodeManagerCl")
public class ReliableNodeManagerCl implements IReliableNodeManagerCl {

    private final ZooKeeper zk;
    public ReliableNodeManagerCl(ZooKeeper zk) {
        this.zk = zk;
    }

    @Override
    public String getVectorServiceUrl(String vectorID) {
        try {
            String rmPath = "/resource-managers/rm" + vectorID;

            // Fetch the 'url' znode for the given vectorID
            String urlPath = rmPath + "/url";
            Stat stat = new Stat();
            byte[] data = zk.getData(urlPath, false, stat);

            if (data != null) {
                // Data is a simple string with the URL
                return new String(data, StandardCharsets.UTF_8);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
