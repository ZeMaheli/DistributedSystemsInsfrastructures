package pt.isel.meic.ised.tplm;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.List;

/**
 * Interface for the TwoPhaseLockManager, exposing transactional lock management methods.
 */
@WebService(endpointInterface = "tplm.ITwoPhaseLockManager")
public interface ITwoPhaseLockManager {
    /**
     * Requests locks for a transaction.
     *
     * @param txnID the transaction ID.
     * @param locks the list of locks to acquire.
     * @return 0 if successful, -1 otherwise.
     */
    @WebMethod
    int get_locks(String txnID, List<Lock> locks);

    /**
     * Releases all locks currently held by a transaction.
     *
     * @param txnID the transaction ID.
     */
    @WebMethod
    void release_all_locks(String txnID);
}
