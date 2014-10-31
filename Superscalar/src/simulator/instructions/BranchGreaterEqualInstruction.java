package simulator.instructions;

import simulator.Processor;
import simulator.Status;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class BranchGreaterEqualInstruction extends DecodedInstruction {

    private final int addressToJump;

    private final Status statusRegisterValue;

    private boolean shouldTakeBranch = false;

    public BranchGreaterEqualInstruction(int args[]) {
        super(Operand.BGE);

        // Get address to jump if success
        this.addressToJump = args[1];

        // Get status register value
        this.statusRegisterValue = Status.values()[args[0]];
    }

    @Override
    public void execute(Processor processor) {

        // Should take the branch?
        if(this.statusRegisterValue == Status.EQ ||
                this.statusRegisterValue == Status.GT) {
            this.shouldTakeBranch = true;
        } else {
            this.shouldTakeBranch = false;
        }
    }

    @Override
    public void writeBack(Processor processor) {

        // Modify PC if branch should be taken
        if(this.shouldTakeBranch) {

            if(processor.isInteractive()) {
                // Find the label
                String label = processor.getMemory().addressToLabel(this.addressToJump);
                System.out.println("WRITE-BACK: BGE taking branch to " + label);
            }
            processor.getPc().setValue(this.addressToJump);
        }
    }
}
