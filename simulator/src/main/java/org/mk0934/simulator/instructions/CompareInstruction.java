package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;
import org.mk0934.simulator.Status;

/**
 * CMP - Compare instruction
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class CompareInstruction extends AluInstruction {

    public CompareInstruction(Integer args[], EncodedInstruction encodedInstruction) {
        super(args, Operand.CMP, encodedInstruction);
    }

    @Override
    public void execute(Processor processor) {

        if(lhs < rhs) {
            this.result = Status.LT.getValue();
        } else if(lhs == rhs) {
            this.result = Status.EQ.getValue();
        } else if(lhs > rhs) {
            this.result = Status.GT.getValue();
        }
    }
}
