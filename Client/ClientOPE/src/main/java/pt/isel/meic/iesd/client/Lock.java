package pt.isel.meic.iesd.client;

public class Lock {
    public String vectorId;
    public int element;

    public Lock(String vectorId, int element) {
        this.vectorId = vectorId;
        this.element = element;
    }
}