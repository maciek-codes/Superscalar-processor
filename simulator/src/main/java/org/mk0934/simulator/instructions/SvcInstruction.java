package simulator.instructions;

import simulator.Processor;
import simulator.Register;
import simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class SvcInstruction extends DecodedInstruction {

    public SvcInstruction(Operand op, EncodedInstruction encodedInstruction) {
        super(op, encodedInstruction);
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

    @Override
    public Integer getDestinationRegisterNumber() {
        return null;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return null;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return null;
    }
}
