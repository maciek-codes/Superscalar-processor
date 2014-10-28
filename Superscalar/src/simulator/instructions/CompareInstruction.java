package simulator.instructions;

import simulator.Processor;
import simulator.StatusRegister;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class CompareInstruction extends DecodedInstruction {

    private final int lhs;
    private final int rhs;
    private StatusRegister.Status statusToWrite;

    public CompareInstruction(int args[]) {
        super(Operand.CMP);

        this.lhs = args[0];
        this.rhs = args[1];
    }

    @Override
    public void execute(Processor processor) {

        if(lhs < rhs) {
            this.statusToWrite = StatusRegister.Status.LT;
        } else if(lhs == rhs) {
            this.statusToWrite = StatusRegister.Status.EQ;
        } else if(lhs > rhs) {
            this.statusToWrite = StatusRegister.Status.GT;
        }
    }

    @Override
    public void writeBack(Processor processor) {
        processor.getStatusRegister().setStatus(this.statusToWrite);
    }
}
