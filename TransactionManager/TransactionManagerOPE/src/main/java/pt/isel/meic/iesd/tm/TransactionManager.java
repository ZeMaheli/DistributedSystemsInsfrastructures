package pt.isel.meic.iesd.tm;

import jakarta.jws.WebService;
import java.net.MalformedURLException;
import java.util.ArrayList;

@WebService(endpointInterface = "pt.isel.meic.iesd.tm.ITransaction")
public class TransactionManager implements ITransaction {
    private final TransactionService transactionService;
    private final IXaRepository xaRepository;

    public TransactionManager(TransactionService transactionService, IXaRepository xaRepository) {
        this.transactionService = transactionService;
        this.xaRepository = xaRepository;
    }

    @Override
    public int begin() {
        Transaction newTransaction = transactionService.newTransaction();
        return newTransaction.getID();
    }

    @Override
    public String commit(int transactionID) {
        Transaction transaction = transactionService.getTransaction(transactionID);
        if (transaction.getResources().isEmpty()) return "NO_RESOURCES";
        try {
            ArrayList<IXA> xaManagers = new ArrayList<>();
            boolean prepared = true;
            for (Resource resource : transaction.getResources()) {
                IXA xaManager = xaRepository.getManager(resource);
                if (xaManager.prepare(transactionID)) {
                    xaManagers.add(xaManager);
                }
                else {
                    prepared = false;
                    break;
                }
            }
            boolean failed = false;
            if (prepared) {
                for (IXA xaManager : xaManagers) {
                    if (!xaManager.commit(transactionID)) {
                        failed = true;
                        break;
                    }
                }
            }
            boolean invalid_state = false;
            if (!prepared || failed) {
                for (IXA xaManager : xaManagers) {
                    if (!xaManager.rollback(transactionID)) {
                        invalid_state = true;
                    }
                }
                if (!invalid_state) {
                    return "ROLLED_BACK";
                }
                return "FAILED";
            }
            return "SUCCESS";
        } catch (MalformedURLException e) {
            return "FAILED";
        }
    }

    @Override
    public String rollback(int transactionID) {
        Transaction transaction = transactionService.getTransaction(transactionID);
        if (transaction.getResources().isEmpty()) return "NO_RESOURCES";
        try {
            ArrayList<IXA> xaManagers = new ArrayList<>();
            boolean prepared = true;
            for (Resource resource : transaction.getResources()) {
                IXA xaManager = xaRepository.getManager(resource);
                if (xaManager.prepare(transactionID)) {
                    xaManagers.add(xaManager);
                }
                else {
                    prepared = false;
                    break;
                }
            }
            if (!prepared) return "FAILED";
            boolean rolled = true;
            for (IXA xaManager : xaManagers) {
                if (!xaManager.rollback(transactionID)) {
                    rolled = false;
                }
            }
            if (!rolled) {
                return "FAILED";
            }
            return "ROLLED_BACK";
        } catch (MalformedURLException e) {
            return "FAILED";
        }
    }
}
