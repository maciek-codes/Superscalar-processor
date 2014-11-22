package org.mk0934.simulator;

/**
 * Created by Maciej Kumorek on 10/16/2014.
 */
public class Simulator {

    // Processor
    private Processor processor;

    // Main memory
    private Memory memory;

    public Simulator() {
        this.memory = new Memory();

        this.processor = new Processor(this.memory);
    }

    // Run a simulation of execution of a program
    public void run(Program program, boolean isInteractive) {

        // First load program into the memory
        memory.LoadProgram(program);

        processor.run(isInteractive);
    }
}
