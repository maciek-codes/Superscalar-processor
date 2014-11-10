package simulator.instructions;

import simulator.Processor;
import simulator.Register;
import simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class LoadMemoryInstruction extends DecodedInstruction {

    private final int destinationRegisterNumber;
    private final int offset;
    private final int address;
    private final Integer firstSourceRegister;
    private final Integer secondSourceRegister;
    private Integer result;

    public LoadMemoryInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.LDM, encodedInstruction);

        this.destinationRegisterNumber = args[0];
        this.offset = args[1];
        this.address = args[2];
        this.firstSourceRegister = args[3];
        this.secondSourceRegister = args[4];
    }

    @Override
    public void execute(Processor processor) {

        // Do memory lookup
        int addressToLookup = this.address + this.offset;

        //
        Object value = processor.getMemory().getFromMemory(addressToLookup);

        if(value.getClass() == Integer.class) {
            this.result = (Integer) value;
        }
    }

    @Override
    public void writeBack(Processor processor) {

        final RegisterFile registerFile = processor.getRegisterFile();

        final Register register = registerFile.getRegister(this.destinationRegisterNumber);

        register.setValue(this.result);
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return this.destinationRegisterNumber;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondSourceRegister;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.firstSourceRegister;
    }
}
