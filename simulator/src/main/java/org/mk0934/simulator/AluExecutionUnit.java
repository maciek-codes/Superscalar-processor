package org.mk0934.simulator;

import org.mk0934.simulator.instructions.AluInstruction;
import org.mk0934.simulator.instructions.DecodedInstruction;

import java.util.LinkedList;

/**
 * Execution unit for a processor
 * @author Maciej Kumorek
 */
public class AluExecutionUnit {

    /**
     * Buffer for instructions to execute
     */
    final private LinkedList<AluInstruction> aluIstructionsToExecute;

    /**
     * Buffer for instructions to write-back
     */
    final private LinkedList<DecodedInstruction> instructionsToWriteBack;

    /**
     * ID of the execution unit
     */
    private int id;

    /**
     * Log tag
     */
    private String tag = "EXECUTE";

    /**
     * Processor execution unit belongs to
     */
    final private Processor processor;

    public AluExecutionUnit(LinkedList<AluInstruction> aluIstructionsToExecute,
                            LinkedList<DecodedInstruction> instructionsToWriteBack,
                            Processor processor,
                            int id) {

        // Assign the references to buffers
        this.aluIstructionsToExecute = aluIstructionsToExecute;
        this.instructionsToWriteBack = instructionsToWriteBack;
        this.processor = processor;
        this.id = id;
        this.tag += "(" + id + ")";
    }

    /**
     * Execute
     */
    public void execute() {

        if(this.aluIstructionsToExecute.isEmpty()) {
            Utilities.log(tag, "nothing to do");
            return;
        }

        DecodedInstruction instruction = this.aluIstructionsToExecute.removeFirst();

        Utilities.log(tag, "Executing " + instruction.getEncodedInstruction());

        instruction.execute(this.processor);

        this.instructionsToWriteBack.addLast(instruction);

        this.processor.incrementInstructionCounter();
    }
}
