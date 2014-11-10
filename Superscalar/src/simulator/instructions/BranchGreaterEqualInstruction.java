package simulator.instructions;

import simulator.Processor;
import simulator.Status;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class BranchGreaterEqualInstruction extends BranchInstruction {

    public BranchGreaterEqualInstruction(Integer args[], EncodedInstruction encodedInstruction) {
        super(Operand.BGE, args, encodedInstruction);
    }

    @Override
    protected boolean shouldTakeBranch() {
        // Should take the branch?
        if(this.statusRegisterValue == Status.EQ
                || this.statusRegisterValue == Status.GT) {
            return true;
        } else {
            return false;
        }
    }
}
