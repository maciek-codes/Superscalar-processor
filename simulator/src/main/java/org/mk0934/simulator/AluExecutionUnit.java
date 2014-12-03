package org.mk0934.simulator;

import org.mk0934.simulator.instructions.AluInstruction;

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

    /**
     * Currently processed instruction
     */
    private AluInstruction currentlyExecuted;

    /**
     * Counter for latency simulation
     */
    private int counter;

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

        if(aluIstructionsToExecute.isEmpty() && currentlyExecuted == null) {
            Utilities.log(tag, "nothing to do");
            return;
        }

        // get next instruction
        if(currentlyExecuted == null) {
            currentlyExecuted = aluIstructionsToExecute.peek();
        }

        // Simulate latency
        if(counter < currentlyExecuted.getLatency()) {
            counter++;

            StringBuilder message = new StringBuilder();

            message.append(String.format("Executing %s", currentlyExecuted.getEncodedInstructionString()));

            if(currentlyExecuted.getLatency() > 0) {
                message.append(String.format(" (%d/%d)",  counter, currentlyExecuted.getLatency()));
            }

            Utilities.log(tag, message.toString());
        }

        // Finish execution
        if(counter == currentlyExecuted.getLatency()) {

            // Remove from the queue
            aluIstructionsToExecute.remove(currentlyExecuted);

            // Execute to get result
            currentlyExecuted.execute(processor);

            // Add to write back
            this.processor.getWriteBackBuffer().add(currentlyExecuted);

            // Reset the current instruction pointer
            this.currentlyExecuted = null;

            // Reset the counter
            counter = 0;

            this.processor.incrementInstructionCounter();
        }
    }
}
