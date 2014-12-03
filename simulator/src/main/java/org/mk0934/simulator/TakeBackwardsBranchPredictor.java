package org.mk0934.simulator;

import org.mk0934.simulator.instructions.BranchInstruction;

/**
 * Branch predictor that assumes to take backward branches, and never take forward
 *
 * @author Maciej Kumorek
 */
public class TakeBackwardsBranchPredictor implements BranchPredictor {

    private final Processor processor;

    public TakeBackwardsBranchPredictor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public BranchPredictorResult predictBranch(BranchInstruction branchInstruction) {

        BranchPredictorResult result = new BranchPredictorResult();

        boolean shouldTake = true;

        final Memory mainMemory = this.processor.getMemory();

        int addressToBranch = branchInstruction.getAddressToJump(),
                currentAddress = mainMemory.getInstructionAddress(branchInstruction),
                alternativeAddress = currentAddress + 0x4;

        if(addressToBranch > currentAddress) {
            shouldTake = false;
            int temp = alternativeAddress;
            alternativeAddress = addressToBranch;
            addressToBranch = temp;
        }

        result.setShouldTake(shouldTake);
        result.setAddressPredicted(addressToBranch);
        result.setAlternativeAddress(alternativeAddress);
        return result;
    }

    @Override
    public void updatePredictor(BranchInstruction branchInstruction, boolean wasTaken) {
        // This is static predictor, doesn't care about the result of prediction
        return;
    }
}
