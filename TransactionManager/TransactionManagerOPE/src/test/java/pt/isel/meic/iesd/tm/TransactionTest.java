package pt.isel.meic.iesd.tm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isel.meic.iesd.tm.mock.MockTransactionRepository;
import pt.isel.meic.iesd.tm.mock.MockXa;
import pt.isel.meic.iesd.tm.mock.MockXaRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
public class TransactionTest {
    private MockXa xaManager;
    private TransactionManager transactionManager;

@BeforeEach
    void setUp() {
    MockTransactionRepository transactionRepository = new MockTransactionRepository();
        Transaction t = new Transaction(0);
        t.addResource(new Resource());
        transactionRepository.storeTransaction(t);
        xaManager = new MockXa();
    TransactionService transactionService = new TransactionService(transactionRepository);
    MockXaRepository xaRepository = new MockXaRepository(xaManager);
        transactionManager = new TransactionManager(transactionService, xaRepository);
    }

    @Test
    void successful_commit() {
        // Config
        xaManager.setShouldPrepare(true);
        xaManager.setShouldCommit(true);
        xaManager.setShouldRollback(false);

        // Test
        int transactionID = transactionManager.begin();
        assertEquals("SUCCESS", transactionManager.commitTransaction(transactionID));
    }

    @Test
    void failed_prepare() {
        // Config
        xaManager.setShouldPrepare(false);
        xaManager.setShouldCommit(true);
        xaManager.setShouldRollback(false);

        // Test
        int transactionID = transactionManager.begin();
        assertEquals("ROLLED_BACK", transactionManager.commitTransaction(transactionID));
    }

    @Test
    void failed_commit_but_rolled_back() {
        // Config
        xaManager.setShouldPrepare(true);
        xaManager.setShouldCommit(false);
        xaManager.setShouldRollback(true);

        // Test
        int transactionID = transactionManager.begin();
        assertEquals("ROLLED_BACK", transactionManager.commitTransaction(transactionID));
    }

    @Test
    void failed_commit_and_rollback() {
        // Config
        xaManager.setShouldPrepare(true);
        xaManager.setShouldCommit(false);
        xaManager.setShouldRollback(false);

        // Test
        int transactionID = transactionManager.begin();
        assertEquals("FAILED", transactionManager.commitTransaction(transactionID));
    }

    @Test
    void successful_rollback() {
        // Config
        xaManager.setShouldPrepare(true);
        xaManager.setShouldCommit(false);
        xaManager.setShouldRollback(true);

        // Test
        int transactionID = transactionManager.begin();
        assertEquals("ROLLED_BACK", transactionManager.rollbackTransaction(transactionID));
    }

    @Test
    void failed_rollback() {
        // Config
        xaManager.setShouldPrepare(true);
        xaManager.setShouldRollback(false);

        // Test
        int transactionID = transactionManager.begin();
        assertEquals("FAILED", transactionManager.rollbackTransaction(transactionID));
    }
}
