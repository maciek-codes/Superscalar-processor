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

    final private int desinationRegister;

    public MoveInstruction(int desinationRegister, int value) {
        super(Operand.MOV);
        this.value = value;
        this.desinationRegister = desinationRegister;
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

        // Store value in destination register
        Register register = processor.getRegisterFile().getRegister(this.desinationRegister);
        register.setValue(this.value);
    }
}
