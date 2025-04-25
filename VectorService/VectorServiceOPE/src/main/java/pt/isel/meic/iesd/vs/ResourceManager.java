package pt.isel.meic.iesd.vs;

import jakarta.xml.ws.Service;
import pt.isel.meic.iesd.tm.AxManagerService;
import pt.isel.meic.iesd.tm.IAX;
import pt.isel.meic.iesd.tm.Resource;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

public class ResourceManager {

    private final int ID;
    private final String HOSTNAME;
    private final int PORT;
    static IAX axManager;

    public ResourceManager(int id, String hostname, int port, String tm_hostname, int tm_port) throws MalformedURLException {
        this.ID = id;
        this.HOSTNAME = hostname;
        this.PORT = port;
        URL wsdlUrl = new URL("http://" + tm_hostname + ":" + tm_port + "/AX?wsdl");
        QName serviceName = new QName("http://example.com/soap", "MyService");
        Service service = AxManagerService.create(wsdlUrl, serviceName);
        axManager = service.getPort(IAX.class);
    }

    public void register(int transactionID) {
        // FIXME: Make the jaxws-maven-plugin import the Resource constructor parameters
        Resource resource = new Resource(/*ID, HOSTNAME, PORT*/);
        axManager.register(transactionID, resource);
    }
}
