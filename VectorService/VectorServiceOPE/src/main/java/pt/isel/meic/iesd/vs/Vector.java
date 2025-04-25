package pt.isel.meic.iesd.vs;

import java.util.Arrays;
import java.util.List;

import jakarta.jws.WebService;

@WebService(endpointInterface = "pt.isel.meic.iesd.vectorservice.IVector")
public class Vector implements IVector {

    private static final List<Integer> vector = Arrays.asList(300, 234, 56, 789);
    private final Integer baseLine = _getVariance();

    @Override
    public int read(int transactionID, int pos) {
        // TODO: Ensure this call is only made in the context of a transaction
        System.out.println("Reading from vector position " + pos);
        return vector.get(pos);
    }

    @Override
    public void write(int transactionID, int pos, int n) {
        // TODO: Ensure this call is only made in the context of a transaction
        System.out.println("Writing to vector in position " + pos + " with " + n);
        vector.set(pos, n);
    }

    // ATTENTION: The variance should only be returned on a valid state.
    // This means it can't be called in the middle of a transaction
    // This should be called by the TM in the beginning/end of a transaction
    // To ensure the invariance is kept
    @Override
    public Integer getVariance(int transactionID) {
        // TODO: Ensure this call is only made in the context of a transaction
        return _getVariance();
    }

    private Integer _getVariance() {
        Integer variance = vector.stream().reduce(0, Integer::sum);
        if (baseLine == null) return variance;
        return variance - baseLine;
    }
}
