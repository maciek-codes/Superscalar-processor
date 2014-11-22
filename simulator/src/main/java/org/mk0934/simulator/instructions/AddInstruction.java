package simulator.instructions;

import simulator.Processor;
import simulator.Register;
import simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class AddInstruction extends DecodedInstruction {

    private final int desinationRegisterNumber;
    private final int lhs;
    private final int rhs;
    private final Integer firstSourceRegisterNumber;
    private final Integer secondSourceRegisterNumber;

    private Integer result;

    public AddInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.ADD, encodedInstruction);

        // First argument is source register name
        this.desinationRegisterNumber = args[0];

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
    public void execute(Processor processor) {

        // Perform addition
        this.result = this.lhs + this.rhs;
    }

    @Override
    public void writeBack(Processor processor) {

        if(this.result == null) {
            throw new NullPointerException("Result has not been computed yet. Execute should be called beforehand");
        }

        // Save to destination register
        final RegisterFile registerFile = processor.getRegisterFile();
        final Register register = registerFile.getRegister(this.desinationRegisterNumber);
        register.setValue(this.result);
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return this.desinationRegisterNumber;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondSourceRegisterNumber;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.firstSourceRegisterNumber;
    }
}
