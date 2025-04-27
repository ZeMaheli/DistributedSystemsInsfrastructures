package pt.isel.meic.iesd.tm.mock;

import pt.isel.meic.iesd.tm.IXA;
import pt.isel.meic.iesd.tm.IXaRepository;
import pt.isel.meic.iesd.tm.Resource;


public class MockXaRepository implements IXaRepository {

    private final IXA xaManager;

    public MockXaRepository(IXA xaManager) {
        this.xaManager = xaManager;
    }

    @Override
    public IXA getManager(Resource resource) {
        return xaManager;
    }
}
