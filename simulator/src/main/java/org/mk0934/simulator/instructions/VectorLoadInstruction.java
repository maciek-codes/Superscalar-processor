package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;

/**
 * Vector load instruction
 */
public class VectorLoadInstruction extends VectorInstruction {

    private final int destinationRegisterNumber;
    private final int offset;
    private final int address;
    private final Integer firstSourceRegister;
    private final Integer secondSourceRegister;
    private Integer[] result;

    public VectorLoadInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.VLDM, encodedInstruction);

        this.destinationRegisterNumber = args[0];
        this.offset = args[1];
        this.address = args[2];
        this.firstSourceRegister = args[3];
        this.secondSourceRegister = args[4];
    }

    @Override
    public void execute(Processor processor) {

        // Do memory lookup
        int addressToLookup = this.address + this.offset;

        this.result = new Integer[width];

        for(int i = 0; i < width; i++) {

            Object value = processor.getMemory().getFromMemory((0x4*i) + addressToLookup);

            if (value.getClass() == Integer.class) {
                this.result[i] = (Integer) value;
            }
        }
    }

    @Override
    protected void doWriteBack(Processor processor) {

        final RegisterFile registerFile = processor.getRegisterFile();

        for (int i = 0; i < width; i++) {

            final Register register = registerFile.getRegister(this.destinationRegisterNumber + i);
            register.setValue(this.result[i]);
        }
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return this.destinationRegisterNumber;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondSourceRegister;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.firstSourceRegister;
    }

    @Override
    public int getLatency() {
        return 4;
    }
}
