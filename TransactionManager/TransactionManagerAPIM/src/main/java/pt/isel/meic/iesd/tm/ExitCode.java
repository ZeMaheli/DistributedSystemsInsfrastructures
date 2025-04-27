package pt.isel.meic.iesd.tm;

public enum ExitCode {
    INVALID_ID,
    INVALID_HOSTNAME,
    INVALID_PORT,
    INVALID_ZOOKEEPER_CONNECTION,
    INVALID_TM_URL,
    ZOOKEEPER_EXCEPTION
    ;

    public int value() {
        return this.ordinal();
    }
}
