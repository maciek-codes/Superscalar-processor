package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Globals;
import org.mk0934.simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class StoreMemoryInstruction extends DecodedInstruction {

    private final int valueToStore;
    private final int baseAddress;
    private final int offset;
    private final Integer sourceRegisterNumber;
    private final Integer secondSourceRegisterNumber;
    private int addressToStore = 0x0;

    public StoreMemoryInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.STM, encodedInstruction);

        this.valueToStore = args[1];
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
    public void writeBack(Processor processor) {
       
        if(Globals.IsVerbose) {
            System.out.println("Storing at " + this.valueToStore 
                + " address " + Integer.toHexString(this.addressToStore));
        }

        // Store in memory
        processor.getMemory().saveToMemory(this.valueToStore, this.addressToStore);
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
}
