package simulator.instructions;

import simulator.Processor;
import simulator.Status;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class CompareInstruction extends DecodedInstruction {

    private final int lhs;
    private final int rhs;
    private final int destinationRegisterNumber;
    private Status statusToWrite;

    public CompareInstruction(int args[]) {
        super(Operand.CMP);

        this.destinationRegisterNumber = args[0];
        this.lhs = args[1];
        this.rhs = args[2];
    }

    @Override
    public void execute(Processor processor) {

        if(lhs < rhs) {
            this.statusToWrite = Status.LT;
        } else if(lhs == rhs) {
            this.statusToWrite = Status.EQ;
        } else if(lhs > rhs) {
            this.statusToWrite = Status.GT;
        }
    }

    @Override
    public void writeBack(Processor processor) {

        processor.getRegisterFile().getRegister(this.destinationRegisterNumber).setValue(this.statusToWrite.getValue());
    }
}
