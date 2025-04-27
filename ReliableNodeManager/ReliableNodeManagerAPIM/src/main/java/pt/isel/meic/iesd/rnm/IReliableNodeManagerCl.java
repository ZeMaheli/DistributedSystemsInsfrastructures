package pt.isel.meic.iesd.rnm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(name = "IReliableNodeManagerCl", targetNamespace = "http://pt.isel.meic.iesd.rnmcl")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface IReliableNodeManagerCl {
    @WebMethod
    String getVectorServiceUrl(String vectorID);
}
