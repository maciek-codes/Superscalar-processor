package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class StoreMemoryInstruction extends DecodedInstruction {

    private final int valueToStore;
    private final int baseAddress;
    private final int offset;
    private final Integer sourceRegisterNumber;
    private final Integer secondSourceRegisterNumber;

    public StoreMemoryInstruction(Integer[] args, EncodedInstruction encodedInstruction) {
        super(Operand.STM, encodedInstruction);

        this.valueToStore = args[0];
        this.offset = args[1];
        this.baseAddress = args[2];

        this.sourceRegisterNumber = args[3];
        this.secondSourceRegisterNumber = args[4];
    }

    @Override
    public void execute(Processor processor) {

        // Calculate absolute address
        int address = this.baseAddress + this.offset;

        // Store in memory
        processor.getMemory().saveToMemory(this.valueToStore, address);
    }

    @Override
    public void writeBack(Processor processor) {
        // No write back for store
    }

    @Override
    public Integer getDestinationRegisterNumber() {
        return null;
    }

    @Override
    public Integer getSecondSourceRegisterNumber() {
        return this.secondSourceRegisterNumber;
    }

    @Override
    public Integer getFirstSourceRegisterNumber() {
        return this.sourceRegisterNumber;
    }
}
