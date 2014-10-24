package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public abstract class DecodedInstruction {

    protected Operand op;


    public DecodedInstruction(Operand op) {
        this.op = op;
    }

    public abstract void execute(Processor processor);

    public Operand getOperand() {
        return this.op;
    }
}
