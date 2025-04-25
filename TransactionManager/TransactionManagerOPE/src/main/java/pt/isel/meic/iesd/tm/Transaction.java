package pt.isel.meic.iesd.tm;

import jakarta.jws.WebService;

@WebService(endpointInterface = "pt.isel.meic.iesd.transactionmanager.ITransaction")
public class Transaction implements ITransaction {

    @Override
    public int begin() {
        return 0;
    }

    @Override
    public String commit(int transactionID) {
        return "";
    }

    @Override
    public String rollback(int transactionID) {
        return "";
    }
}
