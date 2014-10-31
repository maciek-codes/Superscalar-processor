package simulator;

import simulator.instructions.DecodedInstruction;
import simulator.instructions.EncodedInstruction;

/**
 * Created by Maciej Kumorek on 9/30/2014.
 */
public class Processor {

    /**
     * Register file in the processor
     */
    private RegisterFile registerFile;

    /**
     * Main memory bus
     */
    private final Memory mainMemory;

    /**
     * Program counter
     */
    private Register pc = new Register();

    /**
     * Flag indicating if the execution should continue
     */
    private boolean isRunning;
    private boolean isInteractive;

    private DecodedInstruction currentInstruction;
    private EncodedInstruction currentEncodedInstruction;

    private int writeBackDestination = 0;
    private int writeBackValue = 0;

    /**
     * Creates new processor
     */
    public Processor(Memory memory) {

        this.mainMemory = memory;
        this.pc.setValue(0x0);
        this.registerFile = new RegisterFile();
    }

    public void run(boolean isInteractive) {

        this.isRunning = true;
        this.isInteractive = isInteractive;

        while(this.isRunning) {

            this.fetch();
            this.decode();
            this.execute();
            this.writeBack();

            if(this.isInteractive) {
                this.dumpRegisterFile();
            }
        }
    }

    /**
     * Fetch stage
     */
    private void fetch() {

        int currentPcValue = this.pc.getValue();

        // Get instruction from memory
        this.currentEncodedInstruction = (EncodedInstruction)this.mainMemory.getFromMemory(currentPcValue);

        if(this.isInteractive) {
            System.out.println("FETCH: Fetched " + this.currentEncodedInstruction.getEncodedInstruction()
                    + " at address " + Integer.toHexString(currentPcValue));
        }

        // Increment PC
        this.pc.setValue(currentPcValue + 0x4);

        if(this.isInteractive) {
            System.out.println("FETCH: Incremented PC to " + Integer.toHexString(this.pc.getValue()));
        }
    }

    /**
     * Decode stage
     */
    private void decode() {

        if(this.isInteractive) {
            System.out.println("DECODE: Decoding " + this.currentEncodedInstruction.getEncodedInstruction());
        }

        // Decode fetched instruction
        this.currentInstruction = this.currentEncodedInstruction.decode(this);
    }

    /**
     * Execute
     */
    private void execute() {

        if(this.isInteractive) {
            System.out.println("EXECUTE: Executing " + this.currentEncodedInstruction.getEncodedInstruction());
        }

        this.currentInstruction.execute(this);
    }

    /**
     * Write back
     */
    private void writeBack() {

        if(this.isInteractive) {
            System.out.println("WRITE-BACK: Writing back " + this.currentEncodedInstruction.getEncodedInstruction());
        }

        this.currentInstruction.writeBack(this);
    }

    /**
     * Set running flag, useful for termination
     * @param running
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    /**
     * Getter for register file
     * @return
     */
    public final RegisterFile getRegisterFile() {
        return this.registerFile;
    }

    public final Memory getMemory() {
        return this.mainMemory;
    }

    public Register getPc() {
        return this.pc;
    }

    public void dumpMemory() {

        System.out.println("Memory dump: ");
        for(int i = 0; i < this.getMemory().getMaxAddress(); i += 0x4) {
            System.out.println("Addr: 0x" + Integer.toHexString(i)
                    + " " + this.getMemory().getFromMemory(i).toString());
        }

    }

    public void dumpRegisterFile() {

        // Dump registers
        final RegisterFile registerFile = this.getRegisterFile();

        System.out.println("Register file dump: ");

        for(int i = 0; i < registerFile.getCount(); i++) {
            Register register = registerFile.getRegister(i);
            int value = register.getValue();
            System.out.print("R" + String.format("%02d", i) + ":\t0x" + Integer.toHexString(value).toUpperCase());

            if((i + 1) % 4 == 0) {
                System.out.print("\n");
            } else {
                System.out.print("\t\t");
            }
        }

        System.out.println("PC:\t0x" + Integer.toHexString(this.getPc().getValue()).toUpperCase());
    }
}
