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
    private Integer result;

    public MultiplyInstruction(int[] args) {
        super(Operand.MUL);

        // First argument is source register name
        this.desinationRegister = args[0];

        // Second argument is first value to multiply
        this.lhs = args[1];

        // Third argument is second value to multiply
        this.rhs = args[2];
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
}
