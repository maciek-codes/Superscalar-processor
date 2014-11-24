package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;

/**
 * MOV - Move value to a register
 *
 * @author Maciej Kumorek
 */
public class MoveInstruction extends AluInstruction {

    public MoveInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(args, Operand.MOV, encodedInstruction);
    }

    /**
     * Execute move instruction
     */
    @Override
    public void execute(Processor processor) {
        // No logic in MOV
        return;
    }

    // MOV should never have second source register
    @Override
    public Integer getSecondSourceRegisterNumber() {

        // Move never has second register as parameter
        return null;
    }
}
