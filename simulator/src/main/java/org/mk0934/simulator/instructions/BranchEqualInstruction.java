package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Status;

/**
 * Created by Maciej Kumorek on 10/31/2014.
 */
public class BranchEqualInstruction extends BranchInstruction {

    public BranchEqualInstruction(Integer args[], EncodedInstruction encodedInstruction) {
        super(Operand.BEQ, args, encodedInstruction);
    }

    @Override
    public boolean shouldTakeBranch() {
        // Should take the branch?
        return this.statusRegisterValue == Status.EQ;

    }
}
