package pt.isel.meic.iesd.tplm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

/**
 * Interface for the TwoPhaseLockManager.
 */
@WebService(name = "ITwoPhaseLockManager", targetNamespace = "http://pt.isel.meic.iesd.tplm")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface ITwoPhaseLockManager {
    /**
     * Releases all locks currently held by a transaction.
     *
     * @param txnID the transaction ID.
     */
    @WebMethod
    void release_all_locks(String txnID);

    /**
     * Requests locks for a transaction.
     *
     * @param txnID the transaction ID.
     */
    @WebMethod
    void get_locks(String txnID, String v1, int pos1, String v2, int pos2);
}
