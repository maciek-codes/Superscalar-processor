package org.mk0934.simulator.units;

import org.mk0934.simulator.*;
import org.mk0934.simulator.instructions.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maciej Kumorek on 11/24/2014.
 */
public class BranchExecutionUnit implements WritebackEvent {

    private class Prediction {

        private final BranchInstruction predictedBranch;
        private final boolean predictedToTake;
        private final int alternativeAddress;
        private final DecodedInstruction blockingInstruction;

        public Prediction(BranchInstruction predictedBranch,
                          DecodedInstruction blockingInstruction,
                          boolean predictedToTake,
                          int alternativeAddress) {
            this.predictedBranch = predictedBranch;
            this.predictedToTake = predictedToTake;
            this.alternativeAddress = alternativeAddress;
            this.blockingInstruction = blockingInstruction;
        }
    }

    private final String tag = "BranchExecUnit";

    private final Processor processor;
    private final List<EncodedInstruction> instructionsToDecode;

    private final LinkedList<Prediction> predictions;
    private final BranchPredictor predictor;

    public BranchExecutionUnit(Processor processor,
        List<EncodedInstruction> instructionsToDecode,
        List<AluInstruction> aluInstructionsToExecute[],
        List<MemoryInstruction> memoryInstructionsToExecute[],
        List<DecodedInstruction> instructionsToWriteBack,
        BranchPredictor predictor) {

        this.processor = processor;
        this.instructionsToDecode = instructionsToDecode;
        List<DecodedInstruction> instructionsToWriteBack1 = instructionsToWriteBack;
        List<MemoryInstruction>[] memoryInstructionsToExecute1 = memoryInstructionsToExecute;
        List<AluInstruction>[] aluInstructionsToExecute1 = aluInstructionsToExecute;

        this.predictions = new LinkedList<Prediction>();

        this.predictor = predictor;
    }

    /**
    *   Execute branch
    *
    *   @return branch taken?
    */
    public boolean execute(BranchInstruction branchInstruction) {

            if(branchInstruction.tryTakeBranch(processor)) {

                Utilities.log("BranchExecUnit", "Branch to " + branchInstruction.getAddressToMove());

                // Discard what is in buffers
                this.instructionsToDecode.clear();

                return true;
            }

            // Increment stats
            this.processor.incrementInstructionCounter();
            this.processor.incrementBranchCounter();

            return false;
    }

    public void predictAndExecute(BranchInstruction branchInstruction, DecodedInstruction blockingInstruction) {


        // Predict branch using current branch predictor
        BranchPredictorResult branchPredictorResult = predictor.predictBranch(branchInstruction);
        this.processor.getPc().setValue(branchPredictorResult.getAddressPredicted());
        this.instructionsToDecode.clear();

        if(branchInstruction.shouldTakeBranch()) {

            Utilities.log(tag, "Predicted branch to 0x" + branchInstruction.getAddressToMove());
            // Discard what is in buffers
        } else {
            Utilities.log(tag, "Predicted not to take the branch to 0x" + branchInstruction.getAddressToMove());
        }

        this.processor.incrementInstructionCounter();

        this.predictions.addLast(new Prediction(
                branchInstruction,
                blockingInstruction,
                branchPredictorResult.isShouldTake(),
                branchPredictorResult.getAlternativeAddress()));

        // Now remember to check
        blockingInstruction.addWriteBackListener(this, branchInstruction);
    }

    /**
     * Check the instruction result and correct branch prediction
     * @param writtenInstruction Instruction that was written
     */
    @Override
    public void onWriteBack(DecodedInstruction writtenInstruction) {

        // We're done with listening to this event
        writtenInstruction.removeWriteBackListener(this);

        Prediction prediction = predictions.removeFirst();

        if(prediction.blockingInstruction != writtenInstruction)
        {
            throw new RuntimeException("Trying to evaluate branch that is after last evaluated");
        }

        // Evaluate the branch
        prediction.predictedBranch.UpdateRegisters(processor);

        // Update the branch predictor
        predictor.updatePredictor(prediction.predictedBranch, prediction.predictedBranch.shouldTakeBranch());

        // Was our prediction incorrect?
        if(prediction.predictedToTake != prediction.predictedBranch.shouldTakeBranch()) {

            Utilities.log(tag, "Branch prediction was incorrect.");
            processor.IncrementMissedBranches();

            for(int i = 0; i < Globals.execution_units_num; i++) {
                this.processor.getAluInstructionsBuffer(i).clear();
                this.processor.getMemoryInstructionsToExecute(i).clear();
            }

            // Remove event handlers from predictions
            for(Prediction p : predictions)
            {
                p.blockingInstruction.removeWriteBackListener(this);
            }

            // Other predictions should be not relevant now, because we branched wrong
            predictions.clear();

            //this.processor.getWriteBackBuffer().clear();
            this.instructionsToDecode.clear();

            int addressToJump = 0;

            // In what way was it incorrect?
            if(!prediction.predictedToTake) {
                Utilities.log(tag, "Branch prediction was incorrect - should  have taken.");
                addressToJump = prediction.predictedBranch.getAddressToJump();
            } else {
                Utilities.log(tag, "Branch prediction was incorrect - should not have taken.");
                addressToJump = prediction.alternativeAddress;
            }

            Utilities.log("BranchExecUnit", "Branch to 0x" + Integer.toHexString(addressToJump));
            this.processor.getPc().setValue(addressToJump);
        } else {
            processor.incrementCorrectBranches();
        }
    }
}
