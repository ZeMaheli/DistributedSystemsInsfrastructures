package pt.isel.meic.iesd.tm;

public enum TransactionState {
    STARTED,
    COMMITTING,
    COMMITTED,
    ROLLING_BACK,
    ROLLED_BACK
}
