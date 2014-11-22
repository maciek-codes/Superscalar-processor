package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;
import org.mk0934.simulator.Status;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class CompareInstruction extends DecodedInstruction {

    private final int lhs;
    private final int rhs;
    private final int destinationRegisterNumber;
    private final Integer firstSourceRegisterNumber;
    private final Integer secondSourceRegisterNumber;

    private Status statusToWrite;

    public CompareInstruction(Integer args[], EncodedInstruction encodedInstruction) {
        super(Operand.CMP, encodedInstruction);

        this.destinationRegisterNumber = args[0];
        this.lhs = args[1];
        this.rhs = args[2];
        this.firstSourceRegisterNumber = args[3];
        this.secondSourceRegisterNumber = args[4];
    }

    @Override
    public void execute(Processor processor) {

        if(lhs < rhs) {
            this.statusToWrite = Status.LT;
        } else if(lhs == rhs) {
            this.statusToWrite = Status.EQ;
        } else if(lhs > rhs) {
            this.statusToWrite = Status.GT;
        }
    }

    @Override
    public void writeBack(Processor processor) {

        int registerNumber = this.destinationRegisterNumber;
        RegisterFile registerFile = processor.getRegisterFile();
        Register register = registerFile.getRegister(registerNumber);
        register.setValue(this.statusToWrite.getValue());
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return this.destinationRegisterNumber;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondSourceRegisterNubmer;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.firstSourceRegisterNumber;
    }
}
