package org.mk0934.simulator.units;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Utilities;
import org.mk0934.simulator.instructions.VectorInstruction;

import java.util.LinkedList;

/**
 * Created by Maciej Kumorek on 12/5/2014.
 */
public class VectorExecutionUnit {

    private final String tag = "VectorExecutionUnit";

    /**
     * Reference to currently executed function
     */
    private VectorInstruction currentlyExecuted;

    /**
     * Counter of execution cycles
     */
    int counter = 0;

    /**
     * Buffer of instructions to execute
     */
    private LinkedList <VectorInstruction> vectorInstructionsToExecute = new LinkedList<>();

    /**
     * Reference to the processor
     */
    private final Processor processor;

    public VectorExecutionUnit(Processor processor) {
        this.processor = processor;
    }

    public LinkedList<VectorInstruction> getReservationStation() {
        return vectorInstructionsToExecute;
    }

    /**
     * Execute instructions in this unit
     */
    public void execute() {

        // Execute the memory instruction
        if(getReservationStation().isEmpty() && currentlyExecuted == null) {
            Utilities.log(tag, "nothing to do");
            return;
        }

        // get next instruction
        if(currentlyExecuted == null) {
            currentlyExecuted = getReservationStation().peek();
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
            getReservationStation().remove(currentlyExecuted);

            currentlyExecuted.execute(processor);
            this.processor.getWriteBackBuffer().add(currentlyExecuted);

            // Reset the reference to currently executed
            this.currentlyExecuted = null;

            // Reset the counter
            counter = 0;

            this.processor.incrementInstructionCounter();
        }
    }
}
