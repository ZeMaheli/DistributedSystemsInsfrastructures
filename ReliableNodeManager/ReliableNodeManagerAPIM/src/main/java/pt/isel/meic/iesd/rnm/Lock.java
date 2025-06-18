package pt.isel.meic.iesd.rnm;

public class Lock {
    public String vectorId;
    public int element;

    public Lock() {
    }

    public Lock(String vectorId, int element) {
        this.vectorId = vectorId;
        this.element = element;
    }
}
