package pt.isel.meic.iesd.vs;

import jakarta.jws.WebService;
import pt.isel.meic.iesd.tm.IXA;

@WebService(endpointInterface = "pt.isel.meic.iesd.tm.IXA")
public class XaManager implements IXA {

    @Override
    public boolean prepare(int transactionID) {
        return false;
    }

    @Override
    public boolean commit(int transactionID) {
        return false;
    }

    @Override
    public boolean rollback(int transactionID) {
        return false;
    }
}

