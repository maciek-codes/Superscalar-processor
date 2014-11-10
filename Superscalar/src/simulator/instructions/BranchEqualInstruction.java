package simulator.instructions;

import simulator.Status;

/**
 * Created by Maciej Kumorek on 10/31/2014.
 */
public class BranchEqualInstruction extends BranchInstruction {

    public BranchEqualInstruction(Integer args[], EncodedInstruction encodedInstruction) {
        super(Operand.BEQ, args, encodedInstruction);
    }

    @Override
    protected boolean shouldTakeBranch() {
        // Should take the branch?
        if(this.statusRegisterValue == Status.EQ) {
            return true;
        }

        return false;
    }
}
