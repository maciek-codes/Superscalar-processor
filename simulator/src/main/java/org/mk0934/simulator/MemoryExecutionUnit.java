package org.mk0934.simulator;

import org.mk0934.simulator.instructions.MemoryInstruction;

import java.util.LinkedList;

/**
 * Execution unit for memory instruction
 */
public class MemoryExecutionUnit {

    /**
     * Reference to the processor
     */
    private final Processor processor;

    /**
     * Currently executed instruction
     */
    private MemoryInstruction currentlyExecuted;

    /**
     * Cycles counter for simulating latency
     */
    private int counter;

    private String tag = "MEM EXECUTE";

    /**
     * Execution unit id
     */
    private final int id;
    MemoryExecutionUnit(Processor processor, int id) {
        this.processor = processor;
        this.counter = 0;
        this.id = id;

        tag += "(" + id + ")";
    }
    /**
     * Execute instructions in this unit
     */
    public void execute() {

        final LinkedList<? extends MemoryInstruction> memoryInstructionsBuffer =
                this.processor.getMemoryInstructionsToExecute(id);

        // Execute the memory instruction
        if(memoryInstructionsBuffer.isEmpty() && currentlyExecuted == null) {
            Utilities.log(tag, "nothing to do");
            return;
        }

        // get next instruction
        if(currentlyExecuted == null) {
            currentlyExecuted = memoryInstructionsBuffer.peek();
        }

        // Simulate latency
        if(counter < currentlyExecuted.getLatency()) {
            counter++;

            Utilities.log(tag, String.format("Executing %s (%d/%d)", currentlyExecuted.getEncodedInstructionString(),
                    counter,
                    currentlyExecuted.getLatency()));
        }

        // Finish execution
        if(counter == currentlyExecuted.getLatency()) {

            // Remove from the queue
            memoryInstructionsBuffer.remove(currentlyExecuted);

            currentlyExecuted.execute(processor);
            this.processor.getWriteBackBuffer().add(currentlyExecuted);
            this.currentlyExecuted = null;

            // Reset the counter
            counter = 0;

            this.processor.incrementInstructionCounter();
        }
    }
}
