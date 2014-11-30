package org.mk0934.simulator;

import org.mk0934.simulator.instructions.*;
import org.mk0934.simulator.Processor;

import java.util.List;

/**
 * Created by Maciej Kumorek on 11/24/2014.
 */
public class BranchExecutionUnit {

    private final Processor processor;
    private final List<EncodedInstruction> instructionsToDecode;
    
    private final List<AluInstruction> aluInstructionsToExecute[];
    private final List<MemoryInstruction> memoryInstructionsToExecute[];
    private final List<DecodedInstruction> instructionsToWriteBack;

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

                if(branchInstruction.getOperand() != Operand.JMP) {
                   for(int i = 0; i < Globals.execution_units_num; i++) {
                       this.aluInstructionsToExecute[i].clear();
                       this.memoryInstructionsToExecute[i].clear();
                   }

                    this.instructionsToWriteBack.clear();
                }

                this.processor.incrementInstructionCounter();

                return true;
            }

            return false;
    }
}
