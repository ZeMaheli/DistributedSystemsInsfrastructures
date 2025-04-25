package pt.isel.meic.iesd.tm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;

@WebService(name = "ITransaction", targetNamespace = "http://pt.isel.meic.iesd.tm.transactionmanager")
@SOAPBinding(style = Style.DOCUMENT)
public interface ITransaction {

    @WebMethod
    int begin();

    @WebMethod
    String commit(int transactionID);

    @WebMethod
    String rollback(int transactionID);

}
