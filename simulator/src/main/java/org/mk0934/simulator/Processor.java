package org.mk0934.simulator;

import org.mk0934.simulator.instructions.*;

import java.util.LinkedList;

/**
 * Created by Maciej Kumorek on 9/30/2014.
 */
public class Processor {

    private final int EXECUTION_UNITS_NUM = 2;

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

    /**
     * Buffers
     */
    private LinkedList<DecodedInstruction> instructionsToExecute[];
    private LinkedList<EncodedInstruction> instructionsToDecode;
    private LinkedList<DecodedInstruction> instructionsToWriteBack[];

    /**
     * Execution units
     */
    private ExecutionUnit executionUnits[];

    /**
     * Write-back units
     */
    private WriteBackUnit writebackUnits[];

    /**
     * Creates new processor
     */
    public Processor(Memory memory) {

        this.mainMemory = memory;
        this.pc.setValue(0x0);
        this.registerFile = new RegisterFile();
        this.instructionsToExecute = new LinkedList[EXECUTION_UNITS_NUM];
        this.instructionsToDecode = new LinkedList<EncodedInstruction>();
        this.instructionsToWriteBack = new LinkedList[EXECUTION_UNITS_NUM];

        this.executionUnits = new ExecutionUnit[EXECUTION_UNITS_NUM];
        this.writebackUnits = new WriteBackUnit[EXECUTION_UNITS_NUM];

        // Initialize buffers
        for(int i = 0; i < EXECUTION_UNITS_NUM; i++) {
            this.instructionsToExecute[i] = new LinkedList<>();
            this.instructionsToWriteBack[i] = new LinkedList<>();
            this.executionUnits[i] = new ExecutionUnit(instructionsToExecute[i], instructionsToWriteBack[i], this, i);
            this.writebackUnits[i] = new WriteBackUnit(instructionsToWriteBack[i], this, i);
        }
    }

    /**
     * Run the processor simulation
     */
    public void run() {

        this.isRunning = true;
        int cycles = 0;
        while(this.isRunning) {

            Utilities.log("Cycle #" + cycles);

            // Write-back
            for(int i = 0; i < EXECUTION_UNITS_NUM; i++) {
                this.writebackUnits[i].writeBack();
            }

            // Execute
            for(int i = 0; i < EXECUTION_UNITS_NUM; i++) {
                executionUnits[i].execute();
            }

            // Decode
            this.decode();

            // Fetch
            this.fetch();

            cycles++;

            if(Globals.IsInteractive) {
                this.dumpRegisterFile();
            }
        }

        System.out.println("Total cycles #" + cycles);
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
            if(Globals.IsInteractive) {
                System.out.println("FETCH: nothing to do");
            }
            return;
        }

        if(Globals.IsInteractive) {
            System.out.println("FETCH: Fetched " + currentEncodedInstruction.getEncodedInstruction()
                    + " at address " + Integer.toHexString(currentPcValue));
        }

        // Increment PC
        this.pc.setValue(currentPcValue + 0x4);

        if(Globals.IsInteractive) {
            System.out.println("FETCH: Incremented PC to " + Integer.toHexString(this.pc.getValue()));
        }

        instructionsToDecode.addLast(currentEncodedInstruction);
    }

    /**
     * Decode stage
     */
    private void decode() {

        if(this.instructionsToDecode.isEmpty()) {
            if(Globals.IsInteractive) {
                System.out.println("DECODE: nothing to do");
            }
            return;
        }

        EncodedInstruction currentEncodedInstruction = this.instructionsToDecode.removeFirst();

        if(Globals.IsInteractive) {
            System.out.println("DECODE: Decoding " + currentEncodedInstruction.getEncodedInstruction());
        }

        // Decode fetched instruction
        DecodedInstruction currentInstruction = currentEncodedInstruction.decode(this);
        Integer sourceRegister1 = currentInstruction.getFirstSourceRegisterNumber();
        Integer sourceRegister2 = currentInstruction.getSecondSourceRegisterNumber();

        // Check if there is a dependency
        for(DecodedInstruction instruction : this.instructionsToExecute[0]) {

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

        for(DecodedInstruction instruction : this.instructionsToWriteBack[0]) {

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

                if(Globals.IsInteractive) {
                    System.out.println("DECODE: Branch to " + branchInstruction.getAddressToMove());
                }

                // Discard what is in buffers
                this.instructionsToDecode.clear();

                if(branchInstruction.getOperand() != Operand.JMP) {
	               this.instructionsToExecute[0].clear();
                   this.instructionsToWriteBack[0].clear();
                } 
                return;
            }
        } else {
            // Add to the buffer
            this.instructionsToExecute[0].addLast(currentInstruction);
        }
    }

    /**
     * Set running flag, useful for termination
     * @param running
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    /**
     * Gets the register file
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
