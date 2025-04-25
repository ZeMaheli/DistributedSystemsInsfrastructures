package pt.isel.meic.iesd.tm;

public class Resource {
    private final int id;
    private final String hostname;
    private final int port;

    public Resource(int id, String hostname, int port) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;
    }

    public int getID() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
