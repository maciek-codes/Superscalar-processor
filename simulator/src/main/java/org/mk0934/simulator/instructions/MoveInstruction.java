package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;
import org.mk0934.simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class MoveInstruction extends DecodedInstruction {

    /**
     * Value to be moved
     */
    final private int valueToMov;

    final private int destinationRegisterNumber;

    final private Integer sourceRegisterNumber;

    public MoveInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.MOV, encodedInstruction);
        
        // Use number not value
        this.destinationRegisterNumber = args[2];
        this.valueToMov = args[1];
        this.sourceRegisterNumber = args[3];
    }

    /**
     * Execute move
     * @param processor
     */
    @Override
    public void execute(Processor processor) {
        // No logic in MOV
        return;
    }

    public void writeBack(Processor processor) {

        // Get register file
        final RegisterFile registerFile = processor.getRegisterFile();

        // Store value in destination register
        Register register = registerFile.getRegister(this.destinationRegisterNumber);

        // Update the value
        register.setValue(this.valueToMov);
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return this.destinationRegisterNumber;
    }

    //
    @Override
    public Integer getSecondSourceRegisterNumber() {

        // Move never has second register as parameter
        return null;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.sourceRegisterNumber;
    }
}
