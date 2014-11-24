package org.mk0934.simulator.instructions;

/**
 * Abstraction for instructions executed
 * by memory execution units
 *
 * @author Maciej Kumorek
 */
public abstract class MemoryInstruction extends DecodedInstruction {

    public MemoryInstruction(Operand op, EncodedInstruction encodedInstruction) {
        super(op, encodedInstruction);
    }


    /**
     * Get latency of memory instruction
     * @return cycles it takes to execute the instruction
     */
    @Override
    public int getLatency() {
        // Assume fixed latency
        return 4;
    }
}
