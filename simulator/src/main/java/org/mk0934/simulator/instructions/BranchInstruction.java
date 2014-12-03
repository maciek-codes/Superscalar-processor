package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Status;

/**
 * Created by Maciej Kumorek on 11/10/2014.
 */
public abstract class BranchInstruction extends DecodedInstruction {

    protected final Integer addressToJump;
    protected final Integer secondRegisterNumber;
    protected final Integer statusRegisterNumber;

    protected Status statusRegisterValue;

    public BranchInstruction(Operand operand, Integer[] args, EncodedInstruction encodedInstruction) {
            super(operand, encodedInstruction);

            // Get address to jump if success
            this.addressToJump = args[1];
            this.secondRegisterNumber = args[3];

            // Get status register value
            this.statusRegisterValue = Status.values()[args[0]];
            this.statusRegisterNumber = args[2];
    }

    @Override
    public void execute(Processor processor) {
        // No need to do anything
    }

    @Override
    protected void doWriteBack(Processor processor)  {
        // No need to do anything
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return null;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondRegisterNumber;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.statusRegisterNumber;
    }

    public abstract boolean shouldTakeBranch();

    public boolean tryTakeBranch(Processor processor) {

        // Modify PC if branch should be taken
        if(shouldTakeBranch()) {
            processor.getPc().setValue(this.addressToJump);
            return true;
        }

        return false;
    }

    public int getAddressToJump() {
        return this.addressToJump;
    }

    public String getAddressToMove() {
        return Integer.toHexString(this.addressToJump);
    }

    @Override
    public int getLatency() {
        return 1;
    }

    public void UpdateRegisters(Processor processor) {

        // Value in register
        int value = processor.getRegisterFile().getRegister(this.getFirstSourceRegisterNumber()).getValue();
        this.statusRegisterValue = Status.values()[value];
    }
}
