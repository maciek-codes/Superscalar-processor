package simulator.instructions;

import simulator.Processor;
import simulator.Register;

import java.util.Objects;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class LoadMemoryInstruction extends DecodedInstruction {

    private final int destinationRegister;
    private final int offset;
    private final int address;
    private Integer result;

    public LoadMemoryInstruction(int[] args) {
        super(Operand.LDM);

        this.destinationRegister = args[0];
        this.offset = args[1];
        this.address = args[2];
    }

    @Override
    public void execute(Processor processor) {

        // Do memory lookup
        int addressToLookup = this.address + this.offset;
        Object value = processor.getMemory().getFromMemory(addressToLookup);
        if(value.getClass() == Integer.class) {
            this.result = (Integer) value;
        }
    }

    @Override
    public void writeBack(Processor processor) {

        Register reg = processor.getRegisterFile().getRegister(this.destinationRegister);

        reg.setValue(this.result);
    }
}
