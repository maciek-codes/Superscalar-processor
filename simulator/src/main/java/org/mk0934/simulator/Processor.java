package org.mk0934.simulator;

import com.sun.webpane.sg.prism.UtilitiesImpl;
import org.mk0934.simulator.instructions.*;

import java.util.ArrayList;
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
     * No instructions executed
     */
    private int instructionExecutedCount;

    /**
     * No cycles
     */
    int cycles = 0;

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
            boolean decodedFirst = this.decode(0);
            if(decodedFirst) {
                // Try decoding next one
                this.decode(1);
            }

            // Fetch
            this.fetch();

            cycles++;

            if(Globals.IsInteractive) {
                this.dumpRegisterFile();
            }


            if(areQueuesEmpty()) {
                isRunning = false;
            }
        }

        printStatistics();

    }

    /**
     * Checks if all queues are empty
     * @return
     */
    private boolean areQueuesEmpty() {

        return this.instructionsToDecode.isEmpty() & this.areExecuteQueuesEmpty() &
                this.areWriteBackQueuesEmpty();
    }

    /**
     * Print out overall statistics
     */
    private void printStatistics() {

        System.out.println("--- STATISTICS ---");
        System.out.println(String.format("Total cycles: %d", cycles));
        System.out.println(String.format("Total instructions executed: %d", instructionExecutedCount));
        System.out.println(
                String.format("IPC (Instructions per cycle): %.3f", instructionExecutedCount / (double)cycles));
        System.out.println(
                String.format("CPI (Cycles per instruction): %.3f", cycles / (double)instructionExecutedCount));
    }

    /**
     * Fetch stage
     */
    private void fetch() {

        // Get the PC value
        int currentPcValue = this.pc.getValue();

        // Encoded instruction we will try to fetch fom the memory
        EncodedInstruction currentEncodedInstruction;

        // Try to get the instruction from memory
        try {
            currentEncodedInstruction = (EncodedInstruction) this.mainMemory.getFromMemory(currentPcValue);

            Utilities.log("FETCH", "Fetched " + currentEncodedInstruction.getEncodedInstruction()
                    + " at address " + Integer.toHexString(currentPcValue));
        } catch (ClassCastException ex) {
            // We reached memory that isn't instructions
            Utilities.log("FETCH", "nothing to do");
            return;
        }

        // Increment PC
        this.pc.setValue(currentPcValue + 0x4);
        Utilities.log("FETCH", "Incremented PC to " + Integer.toHexString(this.pc.getValue()));

        instructionsToDecode.addLast(currentEncodedInstruction);

        if(instructionsToDecode.size() % EXECUTION_UNITS_NUM != 0) {
            // Fetch one more instruction
            this.fetch();
        }
    }

    /**
     * Decode stage
     */
    private boolean decode(int id) {

        if(this.instructionsToDecode.isEmpty()) {
            Utilities.log("DECODE", "nothing to do");
            return false;
        }

        // Get next encoded instruction from the buffer to be decoded
        EncodedInstruction currentEncodedInstruction = this.instructionsToDecode.removeFirst();

        Utilities.log("DECODE", "Decoding " + currentEncodedInstruction.getEncodedInstruction());

        // Decode fetched instruction
        DecodedInstruction currentInstruction = currentEncodedInstruction.decode(this);
        Integer sourceRegister1 = currentInstruction.getFirstSourceRegisterNumber();
        Integer sourceRegister2 = currentInstruction.getSecondSourceRegisterNumber();

        // If SVC, other queues need to be empty
        if(currentInstruction.getOperand() == Operand.SVC && (!this.areWriteBackQueuesEmpty() ||
        !this.areExecuteQueuesEmpty())) {

            // Stall, we need to wait for the result
            this.instructionsToDecode.addFirst(currentEncodedInstruction);
            Utilities.log("DECODE", "Can't decode SVC");
            return false;
        }

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
                Utilities.log("DECODE", "Can't decode, there's dependency in "
                        + currentEncodedInstruction.getEncodedInstruction());
                return false;
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
                Utilities.log("DECODE", "Can't decode, there's dependency in "
                        + currentEncodedInstruction.getEncodedInstruction());
                return false;
            }
        }

        // Check if it's a branch, if so, take try it here
        if(currentInstruction instanceof BranchInstruction) {
            BranchInstruction branchInstruction = (BranchInstruction)currentInstruction;

            if(branchInstruction.tryTakeBranch(this)) {

                Utilities.log("DECODE", "Branch to " + branchInstruction.getAddressToMove());

                // Discard what is in buffers
                this.instructionsToDecode.clear();

                if(branchInstruction.getOperand() != Operand.JMP) {
                   for(int i = 0; i < EXECUTION_UNITS_NUM; i++) {
                       this.instructionsToExecute[i].clear();
                       this.instructionsToWriteBack[i].clear();
                   }
                } 
                return false;
            }

        } else {
            // Add to the buffer
            this.instructionsToExecute[id].addLast(currentInstruction);
            return true;
        }

        return false;
    }

    private boolean areWriteBackQueuesEmpty() {

        boolean result = true;

        for (LinkedList<?> queue : instructionsToWriteBack) {
            result = result & queue.isEmpty();
        }

        return result;
    }

    private boolean areExecuteQueuesEmpty() {

        boolean result = true;

        for (LinkedList<?> queue : instructionsToExecute) {
            result = result & queue.isEmpty();
        }

        return result;
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

    public void incrementInstructionCounter() {
        this.instructionExecutedCount += 1;
    }
}
