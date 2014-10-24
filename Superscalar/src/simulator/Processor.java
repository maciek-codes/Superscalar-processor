package simulator;

import simulator.instructions.DecodedInstruction;
import simulator.instructions.EncodedInstruction;
import simulator.instructions.Instruction;

/**
 * Created by Maciej Kumorek on 9/30/2014.
 */
public class Processor {

    /**
     * Register file in the processor
     */
    private RegisterFile regFile;

    /**
     * Main memory bus
     */
    private final Memory mainMemory;

    /**
     * Program counter
     */
    private Register pc = new Register();
    private boolean isRunning;

    private DecodedInstruction currentInstruction;
    private EncodedInstruction currentEncodedInstruction;

    /**
     * Creates new processor
     */
    public Processor(Memory memory) {

        this.mainMemory = memory;
        this.pc.setValue(0x0);
        this.regFile = new RegisterFile();
    }

    public void run() {

        this.isRunning = true;

        while(this.isRunning) {
            this.fetch();
            this.decode();
            this.execute();
            this.writeBack();
        }
    }

    /**
     * Fetch stage
     */
    private void fetch() {

        int currentPcValue = this.pc.getValue();

        // Get instruction from memory
        this.currentEncodedInstruction = (EncodedInstruction)this.mainMemory.getFromMemory(currentPcValue);

        // Increment PC
        this.pc.setValue(currentPcValue + 0x4);
    }

    /**
     * Decode stage
     */
    private void decode() {
        this.currentInstruction = this.currentEncodedInstruction.decode();
    }

    /**
     * Execute
     */
    private void execute() {

        this.currentInstruction.execute(this);
    }

    /**
     * Write back
     */
    private void writeBack() {

    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }
}
