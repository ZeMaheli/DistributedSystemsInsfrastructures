package pt.isel.meic.iesd.tm.mock;

import pt.isel.meic.iesd.tm.ITransactionRepository;
import pt.isel.meic.iesd.tm.Transaction;

import java.util.ArrayList;

public class MockTransactionRepository implements ITransactionRepository {

    private final ArrayList<Transaction> transactions = new ArrayList<>();

    @Override
    public Transaction getTransaction(int transactionID) {
        return transactions.stream()
                .filter(t -> t.getID() == transactionID)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void storeTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
