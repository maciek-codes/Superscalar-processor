package org.mk0934.simulator;

import org.mk0934.simulator.instructions.*;
import org.mk0934.simulator.Processor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

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
    
    private final List<AluInstruction> aluInstructionsToExecute[];
    private final List<MemoryInstruction> memoryInstructionsToExecute[];
    private final List<DecodedInstruction> instructionsToWriteBack;
    private final LinkedList<Prediction> predictions;


    public BranchExecutionUnit(Processor processor,
        List<EncodedInstruction> instructionsToDecode,
        List<AluInstruction> aluInstructionsToExecute[],
        List<MemoryInstruction> memoryInstructionsToExecute[],
        List<DecodedInstruction> instructionsToWriteBack) {

        this.processor = processor;
        this.instructionsToDecode = instructionsToDecode;
        this.instructionsToWriteBack = instructionsToWriteBack;
        this.memoryInstructionsToExecute = memoryInstructionsToExecute;
        this.aluInstructionsToExecute = aluInstructionsToExecute;

        this.predictions = new LinkedList<Prediction>();
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

                // Increment stats
                this.processor.incrementInstructionCounter();

                return true;
            }

            return false;
    }

    public void predictAndExecute(BranchInstruction branchInstruction, DecodedInstruction blockingInstruction) {

        // Our alternative is to not take branch, so PC +4
        int alternativeAddress = this.processor.getMemory().getInstructionAddress(branchInstruction) + 0x4;

        Utilities.log(tag, "Predicted branch to " + branchInstruction.getAddressToMove());

        this.processor.getPc().setValue(branchInstruction.getAddressToJump());
        Utilities.log(tag, "PC set to " + branchInstruction.getAddressToMove());

        // Discard what is in buffers
        this.instructionsToDecode.clear();

        // For now predict to take all branches
        this.predictions.addLast(new Prediction(branchInstruction, blockingInstruction, true, alternativeAddress));

        // Now remember to check
        blockingInstruction.addWriteBackListener(this, branchInstruction);
    }

    /**
     * Check the instruction result and correct branch prediction
     * @param writtenInstruction
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

        // Was our prediction incorrect?
        if(prediction.predictedToTake != prediction.predictedBranch.shouldTakeBranch()) {

            Utilities.log(tag, "Branch prediction was incorrect.");

            this.processor.getWriteBackBuffer().clear();
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

            this.instructionsToWriteBack.clear();
            this.instructionsToDecode.clear();

            int addressToJump = 0;

            // In what way was it incorrect?
            if(prediction.predictedToTake == false) {
                Utilities.log(tag, "Branch prediction was incorrect - should  have taken.");
                addressToJump = prediction.predictedBranch.getAddressToJump();
            } else {
                Utilities.log(tag, "Branch prediction was incorrect - should not have taken.");
                addressToJump = prediction.alternativeAddress;
            }

            Utilities.log("BranchExecUnit", "Branch to 0x" + Integer.toHexString(addressToJump));
            this.processor.getPc().setValue(addressToJump);
        }
    }
}
