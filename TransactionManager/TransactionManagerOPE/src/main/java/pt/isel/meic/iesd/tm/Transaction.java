package pt.isel.meic.iesd.tm;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private final Integer ID;
    private TransactionState state;
    private final ArrayList<Resource> resources = new ArrayList<>();

    public Transaction(Integer ID) {
        this.ID = ID;
        this.state = TransactionState.STARTED;
    }

    public Integer getID() {
        return ID;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
    }

    public List<Resource> getResources() {
        return resources.stream().toList();
    }
}
