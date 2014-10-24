package simulator.instructions;

import simulator.Processor;
import simulator.Register;
import simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class MoveInstruction extends DecodedInstruction {

    final private int destinationRegister;
    private int sourceRegister = -1;
    final private int value;

    public MoveInstruction(String destinationRegisterName, int value) {
        super(Operand.MOV);

        this.destinationRegister =  this.parseRegisterNumber(destinationRegisterName);
        this.value = value;
    }

    public MoveInstruction(String destinationRegisterName, String sourceRegisterName) {
        this(destinationRegisterName, 0);

        this.sourceRegister = this.parseRegisterNumber(sourceRegisterName);
    }

    /**
     * Execute move
     * @param processor
     */
    @Override
    public void execute(Processor processor) {

        final RegisterFile registerFile = processor.getRegisterFile();

        // Find new value (in register or immediate)
        int newValue;
        if(this.sourceRegister >= 0) {
            newValue = registerFile.getRegister(sourceRegister).getValue();
        } else {
            newValue = this.value;
        }

        // Move to destination
        registerFile.getRegister(destinationRegister).setValue(newValue);
    }
}
