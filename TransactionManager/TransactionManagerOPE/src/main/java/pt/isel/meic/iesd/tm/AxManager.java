package pt.isel.meic.iesd.tm;

import jakarta.jws.WebService;

@WebService(endpointInterface = "pt.isel.meic.iesd.tm.IAX")
public class AxManager implements IAX {
    private final TransactionService transactionService;

    public AxManager(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void register(int transactionID, Resource resource) {
        transactionService.registerResourceManager(transactionID, resource);
    }

    @Override
    public void unregister(int transactionID, Resource resource) {
        transactionService.unregisterResourceManager(transactionID, resource);
    }
}
