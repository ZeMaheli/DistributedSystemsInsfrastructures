package pt.isel.meic.iesd.vs;

public interface IResourceManager {
    void register(int transactionID);
    void bufferWrite(int transactionID, int pos, int value);
    boolean prepare(int transactionID);
    boolean commit(int transactionID);
    boolean rollback(int transactionID);
    Integer readBuffered(int transactionID, int pos);
    void setOffline();
}
