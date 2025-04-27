package pt.isel.meic.iesd.tplm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

/**
 * Interface for the TwoPhaseLockManager, exposing transactional lock management methods for the Transaction Manager.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface ITPLMTransactionManager {
    /**
     * Releases all locks currently held by a transaction.
     *
     * @param txnID the transaction ID.
     */
    @WebMethod
    void release_all_locks(String txnID);
}
