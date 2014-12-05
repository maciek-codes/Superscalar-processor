package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 12/5/2014.
 */
public class VectorMultiplyInstruction extends VectorInstruction {

    protected final Integer destinationRegisterNumber;
    protected final Integer firstSourceRegisterNumber;
    protected final Integer secondSourceRegisterNumber;
    protected Integer[] result = null;

    public VectorMultiplyInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.VMUL, encodedInstruction);

        // First argument is source register name
        this.destinationRegisterNumber = args[0];

        // First source register number
        this.firstSourceRegisterNumber = args[3];

        // Second source register number
        this.secondSourceRegisterNumber = args[4];
    }

    @Override
    public void execute(Processor processor) {

        final RegisterFile registerFile = processor.getRegisterFile();

        this.result = new Integer[width];

        for (int i = 0; i < width; i++) {
            int lhs = registerFile.getRegister(this.firstSourceRegisterNumber + i).getValue();
            int rhs = registerFile.getRegister(this.secondSourceRegisterNumber + i).getValue();

            result[i] = lhs * rhs;
        }
    }

    @Override
    protected void doWriteBack(Processor processor) {

        if(this.result == null) {
            throw new NullPointerException("Result has not been computed yet. Execute should be called beforehand");
        }

        // Save to destination register
        final RegisterFile registerFile = processor.getRegisterFile();

        for (int i = 0; i < width; i++) {
            final Register register = registerFile.getRegister(getDestinationRegisterNumber() + i);
            register.setValue(this.result[i]);
        }
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

    @Override
    public int getLatency() {
        return 2;
    }
}
