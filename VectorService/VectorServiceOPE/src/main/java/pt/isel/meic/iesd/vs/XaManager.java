package pt.isel.meic.iesd.vs;

import jakarta.jws.WebService;
import pt.isel.meic.iesd.tm.IXA;

@WebService(endpointInterface = "pt.isel.meic.iesd.tm.IXA")
public class XaManager implements IXA {
    private final IResourceManager resourceManager;

    public XaManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public boolean prepare(int transactionID) {
        System.out.println("XaManager: prepare transaction " + transactionID);
        return resourceManager.prepare(transactionID);
    }

    @Override
    public boolean commit(int transactionID) {
        System.out.println("XaManager: commit transaction " + transactionID);
        return resourceManager.commit(transactionID);
    }

    @Override
    public boolean rollback(int transactionID) {
        System.out.println("XaManager: rollback transaction " + transactionID);
        return resourceManager.rollback(transactionID);
    }
}

