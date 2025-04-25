package pt.isel.meic.iesd.tm;

import java.util.List;

public class TransactionRepository {

    public List<Transaction> transactions = List.of();

    public Transaction newTransaction() {
        Transaction newTransaction = new Transaction(transactions.size());
        transactions.add(newTransaction);
        return newTransaction;
    }

    public void registerResourceManager(int transactionID, Resource resource) {
        Transaction transaction = transactions.stream()
                .filter(t -> t.getID() == transactionID)
                .findFirst()
                .orElse(null);
        if (transaction == null) return;
        transaction.addResourceManager(resource);
    }

    public void unregisterResourceManager(int transactionID, Resource resource) {
        Transaction transaction = transactions.stream()
                .filter(t -> t.getID() == transactionID)
                .findFirst()
                .orElse(null);
        if (transaction == null) return;
        transaction.removeResourceManager(resource);
    }

}
