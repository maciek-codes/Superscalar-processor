package simulator.instructions;

import simulator.Processor;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class StoreMemoryInstruction extends DecodedInstruction {

    private final int valueToStore;
    private final int baseAddress;
    private final int offset;

    public StoreMemoryInstruction(int[] args) {
        super(Operand.STM);

        this.valueToStore = args[0];
        this.offset = args[1];
        this.baseAddress = args[2];
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
}
