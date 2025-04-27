package pt.isel.meic.iesd.tm;

public interface ITransactionRepository {
    Transaction getTransaction(int transactionID);
    void storeTransaction(Transaction transaction);
}
