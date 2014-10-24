package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class MultiplyInstruction extends DecodedInstruction {
    public MultiplyInstruction(Operand operand) {
        super(operand);
    }

    @Override
    public void execute(Processor processor) {

    }
}
