package pt.isel.meic.iesd.rnm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.util.List;

@WebService(name = "IReliableNodeManagerTPLM", targetNamespace = "http://pt.isel.meic.iesd.rnmtplm")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface IReliableNodeManagerTPLM {
    @WebMethod
    boolean checkRmStatus(String vectorId);

    @WebMethod
    String getHolder(String path, String vectorId, int element) throws Exception;

    @WebMethod
    void setHolder(String path, String vectorId, int element, String txnID) throws Exception;

    @WebMethod
    void addLockHeld(String txnID, Lock lock) throws Exception;

    @WebMethod
    List<Lock> getLocksHeld(String path, String txnID) throws Exception;

    @WebMethod
    List<String> getPendingTransactions(String path);

    @WebMethod
    List<Lock> getPendingRequest(String txnID);

    @WebMethod
    void addPendingRequest(String path, String txnID, List<Lock> locks) throws Exception;

    @WebMethod
    void removePendingRequest(String path, String txnID) throws Exception;

    @WebMethod
    void clearHolder(String path, String vectorId, int element) throws Exception;

    @WebMethod
    void clearLocksHeld(String path, String txnID) throws Exception;
}
