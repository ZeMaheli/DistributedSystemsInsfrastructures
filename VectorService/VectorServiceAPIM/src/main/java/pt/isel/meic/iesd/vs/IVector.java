package pt.isel.meic.iesd.vs;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;

@WebService(name = "IVector", targetNamespace = "http://pt.isel.meic.iesd.vs")
@SOAPBinding(style = Style.DOCUMENT)
public interface IVector {

    @WebMethod
    int read(int transactionID, int pos);

    @WebMethod
    void write(int transactionID, int pos, int n);

    @WebMethod
    Integer getVariance(int transactionID);

}
