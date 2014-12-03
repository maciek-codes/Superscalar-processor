package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Status;

/**
 * BGT - Branch Greater than instruction
 *
 * @author Maciej Kumorek
 */
public class BranchGreaterThanInstruction extends BranchInstruction {

    public BranchGreaterThanInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.BGT, args, encodedInstruction);
    }

    @Override
    public boolean shouldTakeBranch() {
        // Should take the branch?
        if(this.statusRegisterValue == Status.GT) {
            return true;
        } else {
            return false;
        }
    }
}
