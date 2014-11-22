package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Status;

/**
 * Created by Maciej Kumorek on 10/31/2014.
 */
public class BranchGreaterThanInstruction extends BranchInstruction {

    public BranchGreaterThanInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.BGT, args, encodedInstruction);
    }

    @Override
    protected boolean shouldTakeBranch() {
        // Should take the branch?
        if(this.statusRegisterValue == Status.GT) {
            return true;
        } else {
            return false;
        }
    }
}
