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
            for (IXA xaManager : xaManagers) {
                if (prepared) {
                    if (!xaManager.commit(transactionID)) {
                        failed = true;
                        break;
                    }
                } else {
                    xaManager.rollback(transactionID);
                }
            }
            if (failed) {
                for (IXA xaManager : xaManagers) {
                    xaManager.rollback(transactionID);
                }
            }
            if (failed || !prepared) {
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
            boolean rolled = true;
            for (IXA xaManager : xaManagers) {
                if (xaManager.rollback(transactionID)) {
                    rolled = false;
                }
            }
            if (!rolled) {
                return "FAILED";
            }
            return "SUCCESS";
        } catch (MalformedURLException e) {
            return "FAILED";
        }
    }
}
