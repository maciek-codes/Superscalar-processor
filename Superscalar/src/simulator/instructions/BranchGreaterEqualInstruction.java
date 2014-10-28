package simulator.instructions;

import simulator.Processor;
import simulator.StatusRegister;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class BranchGreaterEqualInstruction extends DecodedInstruction {

    private final int addressToJump;

    private final StatusRegister.Status statusRegisterValue;

    private boolean shouldTakeBranch = false;

    public BranchGreaterEqualInstruction(int addressToJump, StatusRegister.Status statusRegisterValue) {
        super(Operand.BGE);

        // Get address to jump if success
        this.addressToJump = addressToJump;

        // Get status register value
        this.statusRegisterValue = statusRegisterValue;
    }

    @Override
    public void execute(Processor processor) {

        // Should take the branch?
        if(this.statusRegisterValue == StatusRegister.Status.EQ ||
                this.statusRegisterValue == StatusRegister.Status.GT) {
            this.shouldTakeBranch = true;
        } else {
            this.shouldTakeBranch = false;
        }
    }

    @Override
    public void writeBack(Processor processor) {

        // Modify PC if branch should be taken
        if(this.shouldTakeBranch) {
            processor.getPc().setValue(this.addressToJump);
        }
    }
}
