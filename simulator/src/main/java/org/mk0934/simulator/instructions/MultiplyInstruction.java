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


    /**
     * Get latency of ALU instruction
     * @return cycles it takes to execute the instruction
     */
    @Override
    public int getLatency() {
        // MUL will take longer
        return 2;
    }
}
