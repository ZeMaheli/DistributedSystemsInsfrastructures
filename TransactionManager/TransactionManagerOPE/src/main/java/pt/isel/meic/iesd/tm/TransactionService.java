package pt.isel.meic.iesd.tm;

public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private int tCounter = 0;

    public TransactionService(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction newTransaction() {
        Transaction newTransaction = new Transaction(tCounter++);
        transactionRepository.storeTransaction(newTransaction);
        return newTransaction;
    }

    public Transaction getTransaction(int transactionID) {
        return transactionRepository.getTransaction(transactionID);
    }

    public void registerResourceManager(int transactionID, Resource resource) {
        Transaction transaction = transactionRepository.getTransaction(transactionID);
        if (transaction == null) return;
        if (transaction.getResources().contains(resource)) return;
        transaction.addResource(resource);
        transactionRepository.storeTransaction(transaction);
    }

    public void unregisterResourceManager(int transactionID, Resource resource) {
        Transaction transaction = transactionRepository.getTransaction(transactionID);
        if (transaction == null) return;
        transaction.removeResource(resource);
    }

}
