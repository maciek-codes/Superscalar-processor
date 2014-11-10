package simulator.instructions;

import simulator.Processor;
import simulator.Register;
import simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class MoveInstruction extends DecodedInstruction {

    /**
     * Value to be moved
     */
    final private int value;

    final private int destinationRegisterNumber;

    final private Integer sourceRegisterNumber;

    public MoveInstruction(int destinationRegisterNumber,
                           Integer sourceRegisterNumber,
                           int value,
                           EncodedInstruction encodedInstruction) {
        super(Operand.MOV, encodedInstruction);
        this.value = value;
        this.sourceRegisterNumber = sourceRegisterNumber;
        this.destinationRegisterNumber = destinationRegisterNumber;
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
        register.setValue(this.value);
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
