package pt.isel.meic.iesd.tm;

import java.util.ArrayList;

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

    public void addResourceManager(Resource resource) {
        resources.add(resource);
    }

    public void removeResourceManager(Resource resource) {
        resources.remove(resource);
    }
}
