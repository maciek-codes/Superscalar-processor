package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class SvcInstruction extends DecodedInstruction {

    public SvcInstruction(Operand op) {
        super(op);
    }

    @Override
    public void execute(Processor processor) {

        processor.setRunning(false);
    }
}
