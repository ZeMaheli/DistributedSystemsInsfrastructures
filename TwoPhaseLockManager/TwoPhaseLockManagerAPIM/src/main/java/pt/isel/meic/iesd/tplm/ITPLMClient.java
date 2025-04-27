package pt.isel.meic.iesd.tplm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.util.List;

/**
 * Interface for the TwoPhaseLockManager client, exposing transactional lock management methods for the Client.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface ITPLMClient {
    /**
     * Requests locks for a transaction.
     *
     * @param txnID the transaction ID.
     * @param locks the list of locks to acquire.
     */
    @WebMethod
    void get_locks(String txnID, List<Lock> locks);
}
