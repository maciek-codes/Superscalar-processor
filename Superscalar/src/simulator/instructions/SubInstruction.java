package simulator.instructions;

import simulator.Processor;
import simulator.Register;
import simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/27/2014.
 */
public class SubInstruction extends DecodedInstruction {

    private final int desinationRegister;
    private final int lhs;
    private final int rhs;

    private Integer result;

    public SubInstruction(int[] args) {
        super(Operand.SUB);

        // First argument is source register name
        this.desinationRegister = args[0];

        // Second argument is first value to add
        this.lhs = args[1];

        // Third argument is second value to add
        this.rhs = args[2];
    }

    @Override
    public void execute(Processor processor) {

        // Perform addition
        this.result = this.lhs - this.rhs;
    }

    @Override
    public void writeBack(Processor processor) {

        if(this.result == null) {
            throw new NullPointerException("Result has not been computed yet. Execute should be called beforehand");
        }

        // Save to destination register
        final RegisterFile registerFile = processor.getRegisterFile();
        final Register register = registerFile.getRegister(this.desinationRegister);
        register.setValue(this.result);
    }
}
