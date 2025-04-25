package pt.isel.meic.iesd.tm;

import jakarta.jws.WebService;

@WebService(endpointInterface = "pt.isel.meic.iesd.tm.IAX")
public class AxManager implements IAX {
    private final TransactionRepository transactionRepository;

    public AxManager(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void register(int transactionID, Resource resource) {
        transactionRepository.registerResourceManager(transactionID, resource);
    }

    @Override
    public void unregister(int transactionID, Resource resource) {
        transactionRepository.unregisterResourceManager(transactionID, resource);
    }
}
