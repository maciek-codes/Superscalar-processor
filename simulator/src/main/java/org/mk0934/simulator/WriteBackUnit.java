package org.mk0934.simulator;

import org.mk0934.simulator.instructions.DecodedInstruction;

import java.util.LinkedList;

/**
 * Write-back unit
 * @author Maciej Kumorek
 */
public class WriteBackUnit {

    /**
     * Buffer for instructions to write-back
     */
    final private LinkedList<DecodedInstruction> instructionsToWriteBack;

    /**
     * Processor execution unit belongs to
     */
    final private Processor processor;

    /**
     * ID of the execution unit
     */
    private int id;

    private String tag = "WRITEBACK";

    /**
     * Initialize the unit
     * @param instructionsToWriteBack buffer with instructions scheduled for write-back
     */
    public WriteBackUnit(LinkedList<DecodedInstruction> instructionsToWriteBack, Processor processor, int id) {

        this.instructionsToWriteBack = instructionsToWriteBack;
        this.processor = processor;
        this.id = id;
        this.tag += "(" + id + ")";
    }

    /**
     * Perform the write-back
     */
    public void writeBack() {

        if(this.instructionsToWriteBack.isEmpty()) {

            // We reached memory that isn't instructions
            Utilities.log(tag, "nothing to do");
            return;
        }

        // Empty the writeback buffer otherwise
        while(!this.instructionsToWriteBack.isEmpty()) {

            // Get next instruction for write back
            DecodedInstruction instruction = this.instructionsToWriteBack.removeFirst();

            Utilities.log(tag, "Writing back " + instruction.getEncodedInstruction());

            // Tell instruction to write itself back
            instruction.writeBack(processor);
        }
    }
}
