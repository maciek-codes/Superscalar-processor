package simulator;

import simulator.instructions.BranchInstruction;
import simulator.instructions.DecodedInstruction;
import simulator.instructions.EncodedInstruction;
import simulator.instructions.Operand;

import java.util.LinkedList;

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

    /**
     * Buffers
     */
    private LinkedList<DecodedInstruction> instructionsToExecute;
    private LinkedList<EncodedInstruction> instructionsToDecode;
    private LinkedList<DecodedInstruction> instructionsToWriteBack;

    /**
     * Creates new processor
     */
    public Processor(Memory memory) {

        this.mainMemory = memory;
        this.pc.setValue(0x0);
        this.registerFile = new RegisterFile();
        this.instructionsToExecute = new LinkedList<DecodedInstruction>();
        this.instructionsToDecode = new LinkedList<EncodedInstruction>();
        this.instructionsToWriteBack = new LinkedList<DecodedInstruction>();
    }

    public void run(boolean isInteractive) {

        this.isRunning = true;
        this.isInteractive = isInteractive;
        int cycles = 0;
        while(this.isRunning) {


            if(this.isInteractive) {
                System.out.println("Cycle #" + cycles);
            }

            this.writeBack();
            this.execute();
            this.decode();
            this.fetch();

            cycles++;

            if(this.isInteractive) {
                this.dumpRegisterFile();
            }
        }

        if(this.isInteractive) {
            System.out.println("Total cycles #" + cycles);
        }
    }

    /**
     * Fetch stage
     */
    private void fetch() {

        int currentPcValue = this.pc.getValue();

        EncodedInstruction currentEncodedInstruction = null;

        try {
            // Get instruction from memory
             currentEncodedInstruction = (EncodedInstruction) this.mainMemory.getFromMemory(currentPcValue);
        } catch (ClassCastException ex) {

            // We reached memory that isn't instructions
            if(this.isInteractive) {
                System.out.println("FETCH: nothing to do");
            }
            return;
        }



        if(this.isInteractive) {
            System.out.println("FETCH: Fetched " + currentEncodedInstruction.getEncodedInstruction()
                    + " at address " + Integer.toHexString(currentPcValue));
        }

        // Increment PC
        this.pc.setValue(currentPcValue + 0x4);

        if(this.isInteractive) {
            System.out.println("FETCH: Incremented PC to " + Integer.toHexString(this.pc.getValue()));
        }

        instructionsToDecode.addLast(currentEncodedInstruction);
    }

    /**
     * Decode stage
     */
    private void decode() {

        if(this.instructionsToDecode.isEmpty()) {
            if(this.isInteractive) {
                System.out.println("DECODE: nothing to do");
            }
            return;
        }

        EncodedInstruction currentEncodedInstruction = this.instructionsToDecode.removeFirst();

        if(this.isInteractive) {
            System.out.println("DECODE: Decoding " + currentEncodedInstruction.getEncodedInstruction());
        }

        // Decode fetched instruction
        DecodedInstruction currentInstruction = currentEncodedInstruction.decode(this);
        Integer sourceRegister1 = currentInstruction.getFirstSourceRegisterNumber();
        Integer sourceRegister2 = currentInstruction.getSecondSourceRegisterNumber();

        // Check if there is a dependency
        for(DecodedInstruction instruction : this.instructionsToExecute) {

            Integer destinationRegister = instruction.getDestinationRegisterNumber();

            // Instruction doesn't write back anything, so no need to worry
            if(destinationRegister == null) {
                continue;
            }

            // Check if some instruction is writing back to our source registers
            if((sourceRegister1 != null && sourceRegister1 == destinationRegister)
                || (sourceRegister2 != null && sourceRegister2 == destinationRegister)) {

                // Stall, we need to wait for the result
                this.instructionsToDecode.addFirst(currentEncodedInstruction);
                return;
            }
        }

        for(DecodedInstruction instruction : this.instructionsToWriteBack) {

            Integer destinationRegister = instruction.getDestinationRegisterNumber();

            // Instruction doesn't write back anything, so no need to worry
            if(destinationRegister == null) {
                continue;
            }

            // Check if some instruction is writing back to our source registers
            if((sourceRegister1 != null && sourceRegister1 == destinationRegister)
                    || (sourceRegister2 != null && sourceRegister2 == destinationRegister)) {

                // Stall, we need to wait for the result
                this.instructionsToDecode.addFirst(currentEncodedInstruction);
                return;
            }
        }

        // Check if it's a branch, if so, take try it here
        if(currentInstruction instanceof BranchInstruction) {
            BranchInstruction branchInstruction = (BranchInstruction)currentInstruction;

            if(branchInstruction.tryTakeBranch(this)) {

                if(this.isInteractive) {
                    System.out.println("DECODE: Branch to " + branchInstruction.getAddressToMove());
                }

                // Discard what is in buffers
                this.instructionsToDecode.clear();

                if(branchInstruction.getOperand() != Operand.JMP) {
	               this.instructionsToExecute.clear();
                   this.instructionsToWriteBack.clear();
                } 
                return;
            }
        } else {
            // Add to the buffer
            this.instructionsToExecute.addLast(currentInstruction);
        }
    }

    /**
     * Execute
     */
    private void execute() {

        if(this.instructionsToExecute.isEmpty()) {
            if(this.isInteractive) {
                System.out.println("EXECUTE: nothing to do");
            }
            return;
        }

        DecodedInstruction instruction = this.instructionsToExecute.removeFirst();

        if(this.isInteractive) {
            System.out.println("EXECUTE: Executing " + instruction.getEncodedInstruction());
        }

        instruction.execute(this);

        this.instructionsToWriteBack.addLast(instruction);
    }

    /**
     * Write back
     */
    private void writeBack() {

        if(this.instructionsToWriteBack.isEmpty()) {

            // We reached memory that isn't instructions
            if(this.isInteractive) {
                System.out.println("WRBCK: nothing to do");
            }
            return;
        }

        DecodedInstruction instruction = this.instructionsToWriteBack.removeFirst();

        if(this.isInteractive) {
            System.out.println("WRBCK: Writing back " + instruction.getEncodedInstruction());
        }

        instruction.writeBack(this);
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

    public boolean isInteractive() {
        return this.isInteractive;
    }
}
