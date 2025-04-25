package pt.isel.meic.iesd.tm;

public enum ExitCode {
    INVALID_HOSTNAME,
    INVALID_PORT;

    int value() {
        return this.ordinal();
    }
}
