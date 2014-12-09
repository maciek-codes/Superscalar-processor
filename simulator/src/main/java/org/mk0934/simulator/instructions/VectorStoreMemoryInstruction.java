package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Memory;
import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 12/5/2014.
 */
public class VectorStoreMemoryInstruction extends VectorInstruction {

    private final int baseAddress;
    private final int offset;
    private final Integer sourceRegisterNumber;
    private final Integer secondSourceRegisterNumber;
    private int addressToStore = 0x0;

    public VectorStoreMemoryInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.VSTM, encodedInstruction);

        int valueToStore = args[1];
        this.offset = args[2];
        this.baseAddress = args[3];

        this.sourceRegisterNumber = args[4];
        this.secondSourceRegisterNumber = args[5];
    }


    @Override
    public void execute(Processor processor) {

        // Calculate absolute address
        this.addressToStore = this.baseAddress + this.offset;
    }

    @Override
    protected void doWriteBack(Processor processor) {

        // Grab reference to the main memory
        final Memory memory = processor.getMemory();

        // Reference to register file
        final RegisterFile registerFile = processor.getRegisterFile();

        for (int i = 0; i < width; i++) {

            final Register register = registerFile.getRegister(this.getFirstSourceRegisterNumber() + i);

            memory.saveToMemory(register.getValue(), this.addressToStore + (i * 0x4));
        }
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return null;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondSourceRegisterNumber;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.sourceRegisterNumber;
    }

    @Override
    public int getLatency() {
        return 4;
    }
}
