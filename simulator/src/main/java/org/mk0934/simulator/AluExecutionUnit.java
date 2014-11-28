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

    public AluExecutionUnit(Processor processor,
                            int id) {

        this.processor = processor;
        this.id = id;
        this.tag += "(" + id + ")";
    }

    /**
     * Execute
     */
    public void execute() {

        LinkedList<AluInstruction> aluIstructionsToExecute = processor.getAluInstructionsBuffer(id);

        if(aluIstructionsToExecute.isEmpty()) {
            Utilities.log(tag, "nothing to do");
            return;
        }

        DecodedInstruction instruction = aluIstructionsToExecute.removeFirst();

        Utilities.log(tag, "Executing " + instruction.getEncodedInstruction());

        instruction.execute(this.processor);

        this.processor.getWriteBackBuffer().addLast(instruction);

        this.processor.incrementInstructionCounter();
    }
}
