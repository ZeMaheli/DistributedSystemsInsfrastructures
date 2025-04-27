package pt.isel.meic.iesd.vs;

import jakarta.xml.ws.Service;
import pt.isel.meic.iesd.rnm.IReliableNodeManagerRM;
import pt.isel.meic.iesd.rnm.ReliableNodeManagerRMService;
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

    private final IReliableNodeManagerRM rnm;

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public ResourceManager(int id, String hostname, int port, String tm_hostname, int tm_port) throws MalformedURLException {
        this.ID = id;
        this.HOSTNAME = hostname;
        this.PORT = port;
        URL wsdlUrl = new URL("http://" + tm_hostname + ":" + tm_port + "/AX?wsdl");
        QName serviceName = new QName("http://tm.iesd.meic.isel.pt/", "AxManagerService");
        Service service = AxManagerService.create(wsdlUrl, serviceName);
        axManager = service.getPort(IAX.class);
        ReliableNodeManagerRMService rnmService = new ReliableNodeManagerRMService();
        rnm = rnmService.getReliableNodeManagerRMPort();
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
        rnm.registerInZookeeper(ID, HOSTNAME, PORT);
    }

    public void setOffline() {
        rnm.setOffline(ID);
    }
}
