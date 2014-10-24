package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class AddInstruction extends DecodedInstruction {

    public AddInstruction(Operand operand) {
        super(operand);
    }

    @Override
    public void execute(Processor processor) {

    }
}
