package org.mk0934.simulator.instructions;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class JumpInstruction extends BranchInstruction {

    public JumpInstruction(int address, EncodedInstruction encodedInstruction) {
        super(Operand.JMP, new Integer[] { 0, address, null, null, null}, encodedInstruction);
    }

    @Override
    public boolean shouldTakeBranch() {
        return true;
    }
}
