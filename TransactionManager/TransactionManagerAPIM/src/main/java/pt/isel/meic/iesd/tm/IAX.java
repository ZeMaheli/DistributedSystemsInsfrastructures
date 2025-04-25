package pt.isel.meic.iesd.tm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(name = "IAX", targetNamespace = "http://pt.isel.meic.iesd.tm.ax")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface IAX {

    @WebMethod
    void register(
            @WebParam(name = "transactionID") int transactionID,
            @WebParam(name = "resource") Resource resource
    );

    @WebMethod
    void unregister(
            @WebParam(name = "transactionID") int transactionID,
            @WebParam(name = "resource") Resource resource
    );
}
