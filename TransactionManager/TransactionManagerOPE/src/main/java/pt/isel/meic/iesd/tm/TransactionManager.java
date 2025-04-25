package pt.isel.meic.iesd.tm;

import jakarta.jws.WebService;

@WebService(endpointInterface = "pt.isel.meic.iesd.tm.ITransaction")
public class TransactionManager implements ITransaction {
    private final TransactionRepository transactionRepository;

    public TransactionManager(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public int begin() {
        Transaction newTransaction = transactionRepository.newTransaction();
        int id = newTransaction.getID();
        String state = newTransaction.getState().name();
        // TODO: Store on a zk node the transaction state ID -> Started
        return newTransaction.getID();
    }

    @Override
    public String commit(int transactionID) {
        // TODO: Ask all parties involved to prepare to commit
        // If every response is ready commit
        // Else ask for rollback
        // TODO: Ask all parties to commit
        // If every response is commited then close
        // Else ask for rollback
        // Return result
        // Result should be a value that represents the resulting state
        return "";
    }

    @Override
    public String rollback(int transactionID) {
        // TODO: Ask all parties involved to rollback
        return "";
    }
}
