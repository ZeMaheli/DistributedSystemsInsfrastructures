package pt.isel.meic.iesd.vs;

import java.util.Arrays;
import java.util.List;

import jakarta.jws.WebService;

@WebService(endpointInterface = "pt.isel.meic.iesd.vs.IVector")
public class Vector implements IVector {
    private final IResourceManager resourceManager;

    private static final List<Integer> vector = Arrays.asList(300, 234, 56, 789);
    private final Integer baseLine = _getVariance();

    public Vector(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public int read(int transactionID, int pos) {
        System.out.println("Reading from vector position " + pos);
        // Ask the RM if there's a buffered value
        Integer bufferedValue = resourceManager.readBuffered(transactionID, pos);
        if (bufferedValue != null) {
            return bufferedValue;
        }

        return vector.get(pos);
    }

    @Override
    public void write(int transactionID, int pos, int n) {
        // Register resource manager inside
        resourceManager.register(transactionID);
        System.out.println("Vector: Delegating write of value " + n + " at pos " + pos + " to ResourceManager.");
        resourceManager.bufferWrite(transactionID, pos, n);
    }

    // Used internally when committing
    public void applyWrite(int pos, int value) {
        System.out.println("Vector: Applying write value " + value + " at pos " + pos);
        vector.set(pos, value);
    }

    // ATTENTION: The variance should only be returned on a valid state.
    // This means it can't be called in the middle of a transaction
    // This should be called by the TM in the beginning/end of a transaction
    // To ensure the invariance is kept
    @Override
    public Integer getVariance(int transactionID) {
        return _getVariance();
    }

    private Integer _getVariance() {
        Integer variance = vector.stream().reduce(0, Integer::sum);
        if (baseLine == null) return variance;
        return variance - baseLine;
    }
}
