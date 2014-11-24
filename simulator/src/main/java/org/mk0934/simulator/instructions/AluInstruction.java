package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;

/**
 * Abstraction for instructions executed by arithmetic and logic unit
 *
 * @author Maciej Kumorek
 */
public abstract class AluInstruction extends DecodedInstruction {

    private final int destinationRegisterNumber;
    protected final int lhs;
    protected final int rhs;
    private final Integer firstSourceRegisterNumber;
    private final Integer secondSourceRegisterNumber;
    protected Integer result = null;

    public AluInstruction(Integer[] args, Operand op, EncodedInstruction encodedInstruction) {
        super(op, encodedInstruction);


        // First argument is source register name
        this.destinationRegisterNumber = args[0];

        // Second argument is first value to add
        this.lhs = args[1];

        // Third argument is second value to add
        this.rhs = args[2];

        // First source register number
        this.firstSourceRegisterNumber = args[3];

        // Second source register number
        this.secondSourceRegisterNumber = args[4];
    }

    @Override
    public void writeBack(Processor processor) {

        if(this.result == null) {
            throw new NullPointerException("Result has not been computed yet. Execute should be called beforehand");
        }

        // Save to destination register
        final RegisterFile registerFile = processor.getRegisterFile();
        final Register register = registerFile.getRegister(
                getDestinationRegisterNumber());
        register.setValue(this.result);
    }


    @Override
    public Integer getDestinationRegisterNumber() {
        return this.destinationRegisterNumber;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondSourceRegisterNumber;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.firstSourceRegisterNumber;
    }

    /**
     * Get latency of ALU instruction
     * @return cycles it takes to execute the instruction
     */
    @Override
    public int getLatency() {
        return 1;
    }
}
