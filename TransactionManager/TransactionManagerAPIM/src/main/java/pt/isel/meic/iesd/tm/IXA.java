package pt.isel.meic.iesd.tm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(name = "IXA", targetNamespace = "http://pt.isel.meic.iesd.tm.xa")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface IXA {

    @WebMethod
    boolean prepare(int transactionID);

    @WebMethod
    boolean commit(int transactionID);

    @WebMethod
    boolean rollback(int transactionID);
}
