package simulator.instructions;

import simulator.Processor;
import simulator.Register;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class MultiplyInstruction extends DecodedInstruction {

    private final int desinationRegister;
    private final int lhs;
    private final int rhs;
    private final Integer sourceRegisterNumber;
    private final Integer secondSourceRegisterNumber;
    private Integer result;

    public MultiplyInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.MUL, encodedInstruction);

        // First argument is source register name
        this.desinationRegister = args[0];

        // Second argument is first value to multiply
        this.lhs = args[1];

        // Third argument is second value to multiply
        this.rhs = args[2];

        this.sourceRegisterNumber = args[3];
        this.secondSourceRegisterNumber = args[4];
    }

    @Override
    public void execute(Processor processor) {
        this.result = lhs * rhs;
    }

    @Override
    public void writeBack(Processor processor) {

        if(this.result == null) {
            throw new RuntimeException("Result not yet computed. Run execute() first.");
        }

        // Write result to destination register
        Register reg = processor.getRegisterFile().getRegister(this.desinationRegister);
        reg.setValue(this.result);
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return this.desinationRegister;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondSourceRegisterNumber;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.sourceRegisterNumber;
    }
}
