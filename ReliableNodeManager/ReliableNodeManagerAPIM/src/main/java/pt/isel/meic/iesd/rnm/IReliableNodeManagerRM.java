package pt.isel.meic.iesd.rnm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(name = "IReliableNodeManagerRM", targetNamespace = "http://pt.isel.meic.iesd.rnmrm")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface IReliableNodeManagerRM {
    @WebMethod
    void registerInZookeeper(int ID, String HOSTNAME, int PORT);

    @WebMethod
    void setOffline(int ID);
}
