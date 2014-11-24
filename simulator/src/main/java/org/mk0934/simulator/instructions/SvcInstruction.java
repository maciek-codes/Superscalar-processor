package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;

/**
 * SVC - Special pseudo-instruction
 *
 * @author Maciej Kumorek
 */
public class SvcInstruction extends DecodedInstruction {

    public SvcInstruction(Operand op, EncodedInstruction encodedInstruction) {
        super(op, encodedInstruction);
    }

    @Override
    public void execute(Processor processor) {

        processor.setRunning(false);
        processor.dumpRegisterFile(false);
        processor.dumpMemory();
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
