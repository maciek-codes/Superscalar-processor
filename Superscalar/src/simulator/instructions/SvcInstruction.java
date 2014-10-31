package simulator.instructions;

import simulator.Processor;
import simulator.Register;
import simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class SvcInstruction extends DecodedInstruction {

    public SvcInstruction(Operand op) {
        super(op);
    }

    @Override
    public void execute(Processor processor) {

        processor.setRunning(false);
        processor.dumpRegisterFile();
        processor.dumpMemory();
    }

    @Override
    public void writeBack(Processor processor) {
        return;
    }
}
