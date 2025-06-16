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
    String getHolder(String vectorId, int element) throws Exception;

    @WebMethod
    void setHolder(String vectorId, int element, String txnID) throws Exception;

    @WebMethod
    void addLockHeld(String txnID, Lock lock) throws Exception;

    @WebMethod
    List<Lock> getLocksHeld(String txnID) throws Exception;

    @WebMethod
    List<String> getPendingTransactions(String vectorId, int element);

    @WebMethod
    List<Lock> getPendingRequest(String txnID);

    @WebMethod
    void addPendingRequest(String txnID, List<Lock> locks) throws Exception;

    @WebMethod
    void removePendingRequest(List<Lock> locks, String txnID) throws Exception;

    @WebMethod
    void clearHolder(String vectorId, int element) throws Exception;

    @WebMethod
    void clearLocksHeld(String txnID) throws Exception;
}
