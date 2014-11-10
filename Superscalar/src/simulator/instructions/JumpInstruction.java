package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class JumpInstruction extends BranchInstruction {

    public JumpInstruction(int address, EncodedInstruction encodedInstruction) {
        super(Operand.JMP, new Integer[] { 0, address, null, null, null}, encodedInstruction);
    }

    @Override
    protected boolean shouldTakeBranch() {
        return true;
    }
}
