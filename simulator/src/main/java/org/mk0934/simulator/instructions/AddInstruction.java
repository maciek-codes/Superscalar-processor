package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class AddInstruction extends AluInstruction {

    public AddInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(args, Operand.ADD, encodedInstruction);
    }

    @Override
    public void execute(Processor processor) {

        // Perform addition
        this.result = this.lhs + this.rhs;
    }
}
