package org.mk0934.simulator;

import org.mk0934.simulator.instructions.BranchInstruction;

/**
 * Branch predictor that always assumes to branch
 *
 * @author Maciej Kumorek
 */
public class AlwaysTrueBranchPredictor implements BranchPredictor {

    private final Processor processor;

    public AlwaysTrueBranchPredictor(Processor processor) {
        this.processor = processor;
    }

    /**
     * This branch predictor always assumes that the branch will be taken
     * @param branchInstruction Branch instruction to predict
     * @return Branch prediction result
     */
    @Override
    public BranchPredictorResult predictBranch(BranchInstruction branchInstruction) {
        BranchPredictorResult result = new BranchPredictorResult();

        final Memory mainMemory = this.processor.getMemory();

        int addressToBranch = branchInstruction.getAddressToJump(),
                currentAddress = mainMemory.getInstructionAddress(branchInstruction),
                alternativeAddress = currentAddress + 0x4;

        // Take it
        result.setShouldTake(true);

        // Predict to branch to wherever the instructions says
        result.setAddressPredicted(addressToBranch);

        // Alternative is not to take the branch
        result.setAlternativeAddress(alternativeAddress);


        return result;
    }

    @Override
    public void updatePredictor(BranchInstruction branchInstruction, boolean wasTaken) {
        // Static branch predictor, doesn't do much
        return;
    }
}
