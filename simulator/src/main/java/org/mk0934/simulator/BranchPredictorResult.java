package org.mk0934.simulator;

/**
 * Created by Maciej Kumorek on 12/3/2014.
 */
public class BranchPredictorResult {

    private boolean shouldTake;
    private int addressPredicted;
    private int alternativeAddress;

    public BranchPredictorResult() {}

    public boolean isShouldTake() {
        return shouldTake;
    }

    public void setShouldTake(boolean shouldTake) {
        this.shouldTake = shouldTake;
    }

    public int getAddressPredicted() {
        return addressPredicted;
    }

    public void setAddressPredicted(int addressPredicted) {
        this.addressPredicted = addressPredicted;
    }

    public void setAlternativeAddress(int alternativeAddress) {
        this.alternativeAddress = alternativeAddress;
    }

    public int getAlternativeAddress() {
        return alternativeAddress;
    }
}
