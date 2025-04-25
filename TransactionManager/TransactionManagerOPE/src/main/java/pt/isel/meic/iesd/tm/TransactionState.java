package pt.isel.meic.iesd.tm;

public enum TransactionState {
    STARTED,
    COMMITING,
    COMMITTED,
    ROLLING_BACK,
    ROLLED_BACK
}
