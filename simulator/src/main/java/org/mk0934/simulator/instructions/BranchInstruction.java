package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Status;

/**
 * Created by Maciej Kumorek on 11/10/2014.
 */
public abstract class BranchInstruction extends DecodedInstruction {

    protected final Integer addressToJump;
    protected final Integer secondRegisterNumber;
    protected final Status statusRegisterValue;
    protected final Integer statusRegisterNumber;

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
    public void writeBack(Processor processor) {
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

    protected abstract boolean shouldTakeBranch();

    public boolean tryTakeBranch(Processor processor) {

        // Modify PC if branch should be taken
        if(shouldTakeBranch()) {
            processor.getPc().setValue(this.addressToJump);
            return true;
        }

        return false;
    }

    public String getAddressToMove() {
        return Integer.toHexString(this.addressToJump);
    }
}
