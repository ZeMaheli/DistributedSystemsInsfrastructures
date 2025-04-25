package pt.isel.meic.iesd.vs;

public enum ExitCode {
    INVALID_HOSTNAME,
    INVALID_PORT;

    int value() {
        return this.ordinal();
    }
}
