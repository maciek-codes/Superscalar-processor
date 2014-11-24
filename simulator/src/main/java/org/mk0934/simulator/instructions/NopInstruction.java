package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;

/**
 * NOP - not an operation
 *
 * @author Maciej Kumorek
 */
public class NopInstruction extends AluInstruction {

    public NopInstruction(Operand op, EncodedInstruction encodedInstruction) {
        super(new Integer[5], op, encodedInstruction);
    }

    @Override
    public void execute(Processor processor) { return;
    }

    @Override
    public void writeBack(Processor processor) {
        return;
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return null;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return null;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return null;
    }

    @Override
    public int getLatency() {
        return 1;
    }
}
