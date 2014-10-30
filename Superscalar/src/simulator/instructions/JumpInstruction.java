package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class JumpInstruction extends DecodedInstruction {

    final int addressToJump;

    public JumpInstruction(int address) {
        super(Operand.JMP);

        this.addressToJump = address;
    }

    @Override
    public void execute(Processor processor) {

    }

    @Override
    public void writeBack(Processor processor) {
        processor.getPc().setValue(this.addressToJump);
    }
}
