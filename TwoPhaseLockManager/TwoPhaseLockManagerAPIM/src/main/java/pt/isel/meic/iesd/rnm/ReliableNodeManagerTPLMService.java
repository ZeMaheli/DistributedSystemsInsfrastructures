
package pt.isel.meic.iesd.rnm;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.WebServiceFeature;


/**
 * This class was generated by the XML-WS Tools.
 * XML-WS Tools 4.0.2
 * Generated source version: 3.0
 * 
 */
@WebServiceClient(name = "ReliableNodeManagerTPLMService", targetNamespace = "http://rnm.iesd.meic.isel.pt/", wsdlLocation = "file:/C:/Users/Lenovo/Desktop/IESD/Trabalho1/IESD_TRAB_1/DistributedSystemsInsfrastructures/TwoPhaseLockManager/TwoPhaseLockManagerAPIM/src/main/resources/jaxws/ReliableNodeManagerTPLMService.wsdl")
public class ReliableNodeManagerTPLMService
    extends Service
{

    private static final URL RELIABLENODEMANAGERTPLMSERVICE_WSDL_LOCATION;
    private static final WebServiceException RELIABLENODEMANAGERTPLMSERVICE_EXCEPTION;
    private static final QName RELIABLENODEMANAGERTPLMSERVICE_QNAME = new QName("http://rnm.iesd.meic.isel.pt/", "ReliableNodeManagerTPLMService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/Users/Lenovo/Desktop/IESD/Trabalho1/IESD_TRAB_1/DistributedSystemsInsfrastructures/TwoPhaseLockManager/TwoPhaseLockManagerAPIM/src/main/resources/jaxws/ReliableNodeManagerTPLMService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        RELIABLENODEMANAGERTPLMSERVICE_WSDL_LOCATION = url;
        RELIABLENODEMANAGERTPLMSERVICE_EXCEPTION = e;
    }

    public ReliableNodeManagerTPLMService() {
        super(__getWsdlLocation(), RELIABLENODEMANAGERTPLMSERVICE_QNAME);
    }

    public ReliableNodeManagerTPLMService(WebServiceFeature... features) {
        super(__getWsdlLocation(), RELIABLENODEMANAGERTPLMSERVICE_QNAME, features);
    }

    public ReliableNodeManagerTPLMService(URL wsdlLocation) {
        super(wsdlLocation, RELIABLENODEMANAGERTPLMSERVICE_QNAME);
    }

    public ReliableNodeManagerTPLMService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, RELIABLENODEMANAGERTPLMSERVICE_QNAME, features);
    }

    public ReliableNodeManagerTPLMService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ReliableNodeManagerTPLMService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns IReliableNodeManagerTPLM
     */
    @WebEndpoint(name = "ReliableNodeManagerTPLMPort")
    public IReliableNodeManagerTPLM getReliableNodeManagerTPLMPort() {
        return super.getPort(new QName("http://rnm.iesd.meic.isel.pt/", "ReliableNodeManagerTPLMPort"), IReliableNodeManagerTPLM.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link jakarta.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IReliableNodeManagerTPLM
     */
    @WebEndpoint(name = "ReliableNodeManagerTPLMPort")
    public IReliableNodeManagerTPLM getReliableNodeManagerTPLMPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://rnm.iesd.meic.isel.pt/", "ReliableNodeManagerTPLMPort"), IReliableNodeManagerTPLM.class, features);
    }

    private static URL __getWsdlLocation() {
        if (RELIABLENODEMANAGERTPLMSERVICE_EXCEPTION!= null) {
            throw RELIABLENODEMANAGERTPLMSERVICE_EXCEPTION;
        }
        return RELIABLENODEMANAGERTPLMSERVICE_WSDL_LOCATION;
    }

}
