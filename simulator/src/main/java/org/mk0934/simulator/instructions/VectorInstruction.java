package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;

/**
 * Base class for vector instructions
 */
public abstract class VectorInstruction extends DecodedInstruction {

    /**
     * Process this many elements
     */
    protected int width = 4;

    public VectorInstruction(Operand op, EncodedInstruction encodedInstruction) {
        super(op, encodedInstruction);
    }
}
