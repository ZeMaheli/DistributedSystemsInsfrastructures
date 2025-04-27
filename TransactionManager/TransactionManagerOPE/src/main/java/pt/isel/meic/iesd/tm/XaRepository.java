package pt.isel.meic.iesd.tm;

import jakarta.xml.ws.Service;
import pt.isel.meic.iesd.tm.gen.XaManagerService;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

public class XaRepository implements IXaRepository {

    @Override
    public IXA getManager(Resource resource) throws MalformedURLException {
        URL wsdlUrl = new URL("http://" + resource.hostname + ":" + resource.port + "/XA?wsdl");
        QName serviceName = new QName("http://tm.iesd.meic.isel.pt/", "XaManagerService");
        Service service = XaManagerService.create(wsdlUrl, serviceName);
        return service.getPort(IXA.class);
    }
}
