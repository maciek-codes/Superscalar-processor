package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class CompareInstruction extends DecodedInstruction {

    private final int lhs;
    private final int rhs;

    public CompareInstruction(int args[]) {
        super(Operand.CMP);

        this.lhs = args[0];
        this.rhs = args[1];
    }

    @Override
    public void execute(Processor processor) {

    }

    @Override
    public void writeBack(Processor processor) {

    }
}
