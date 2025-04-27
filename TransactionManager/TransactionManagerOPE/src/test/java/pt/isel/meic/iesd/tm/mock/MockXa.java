package pt.isel.meic.iesd.tm.mock;

import pt.isel.meic.iesd.tm.IXA;

public class MockXa implements IXA {

    private boolean shouldPrepare;
    private boolean shouldCommit;
    private boolean shouldRollback;

    public void setShouldPrepare(boolean shouldPrepare) {
        this.shouldPrepare = shouldPrepare;
    }

    public void setShouldCommit(boolean shouldCommit) {
        this.shouldCommit = shouldCommit;
    }

    public void setShouldRollback(boolean shouldRollback) {
        this.shouldRollback = shouldRollback;
    }

    @Override
    public boolean prepare(int transactionID) {
        return shouldPrepare;
    }

    @Override
    public boolean commit(int transactionID) {
        return shouldCommit;
    }

    @Override
    public boolean rollback(int transactionID) {
        return shouldRollback;
    }
}
