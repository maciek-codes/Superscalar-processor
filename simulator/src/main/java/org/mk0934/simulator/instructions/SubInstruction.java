package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/27/2014.
 */
public class SubInstruction extends AluInstruction {

    public SubInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(args, Operand.SUB, encodedInstruction);
    }

    @Override
    public void execute(Processor processor) {

        // Perform addition
        this.result = this.lhs - this.rhs;
    }
}
