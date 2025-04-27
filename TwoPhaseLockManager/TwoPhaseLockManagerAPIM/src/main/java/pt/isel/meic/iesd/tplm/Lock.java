package pt.isel.meic.iesd.tplm;

/**
 * Represents a lock request on a specific element inside a vector managed by a Resource Manager.
 */
public class Lock {
    /**
     * The vector ID the lock belongs to.
     */
    public String vectorId;
    /**
     * The index/element inside the vector being locked.
     */
    public int element;

    /**
     * Constructs a Lock instance for a given vector and element.
     *
     * @param vectorId the ID of the vector.
     * @param element  the element position within the vector.
     */
    public Lock(String vectorId, int element) {
        this.vectorId = vectorId;
        this.element = element;
    }
}
