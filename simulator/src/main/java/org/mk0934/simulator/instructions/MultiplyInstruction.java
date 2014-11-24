package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class MultiplyInstruction extends AluInstruction {

    public MultiplyInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(args, Operand.MUL, encodedInstruction);
    }

    @Override
    public void execute(Processor processor) {
        this.result = lhs * rhs;
    }
}
