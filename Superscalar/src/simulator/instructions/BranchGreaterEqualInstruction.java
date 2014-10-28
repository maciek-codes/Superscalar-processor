package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class BranchGreaterEqualInstruction extends DecodedInstruction {

    private final int addressToJump;

    private boolean shouldTakeBranch = false;

    public BranchGreaterEqualInstruction(int[] args) {
        super(Operand.BGE);

        // Get address to jump if success
        this.addressToJump = args[0];
    }

    @Override
    public void execute(Processor processor) {

        // Should take the branch?
    }

    @Override
    public void writeBack(Processor processor) {

        // Modify PC if branch should be taken
        if(this.shouldTakeBranch) {
            processor.getPc().setValue(this.addressToJump);
        }
    }
}
