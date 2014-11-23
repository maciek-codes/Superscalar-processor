package org.mk0934.simulator;

import org.mk0934.simulator.instructions.BranchInstruction;
import org.mk0934.simulator.instructions.DecodedInstruction;
import org.mk0934.simulator.instructions.EncodedInstruction;
import org.mk0934.simulator.instructions.Operand;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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

        int cycleToJumpTo = -1;

        this.isRunning = true;
        while(this.isRunning) {

            // Set register dirty flag to false
            this.registerFile.commit();

            // Increment cycles
            cycles++;

            if(Globals.IsInteractive) {
                boolean gettingCommands = true;

                Scanner keyboard = new Scanner(System.in);
                while(gettingCommands && (cycles == cycleToJumpTo || cycleToJumpTo < 0) ) {
                    // Reset the 'break-point'
                    cycleToJumpTo = -1;
                    String inputLine = keyboard.nextLine().trim().toLowerCase();

                    if (inputLine.equals("c") || inputLine.equals("continue")) {
                        // Continue
                        gettingCommands = false;
                        Globals.IsInteractive = false;
                    } else if(inputLine.equals("n") || inputLine.equals("next")) {
                        gettingCommands = false;
                    } else if(inputLine.startsWith("i ") || inputLine.startsWith("info ")){
                        if(inputLine.contains(" r")) {
                            int registerNumber = Integer.parseInt(inputLine.replaceAll("[^0-9]", ""));
                            System.out.println(String.format("R%d: 0x%x", registerNumber,
                                    this.registerFile.getRegister(registerNumber).getValue()));
                        } else if(inputLine.contains(" mem")) {
                            this.dumpMemory();
                        }
                    } else if(inputLine.startsWith("j ") || inputLine.startsWith("jump ")) {
                        cycleToJumpTo = Integer.parseInt(inputLine.replaceAll("[^0-9]", ""));
                    }
                }
            }

            Utilities.log("Cycle #" + cycles);

            // Write-back
            for(int i = 0; i < EXECUTION_UNITS_NUM; i++) {
                this.writebackUnits[i].writeBack();
            }

            // Execute
            for(int i = 0; i < EXECUTION_UNITS_NUM; i++) {
                executionUnits[i].execute();
            }

            // Execution unit could have terminated
            if(!this.isRunning) {
                break;
            }

            // Decode
            boolean decodedFirst = this.decode(0);
            if(decodedFirst) {
                // Try decoding next one
                this.decode(1);
            }

            // Fetch
            for(int i = 0; i < EXECUTION_UNITS_NUM; i++) {
                this.fetch(i);
            }

            if(Globals.IsVerbose) {
                this.dumpRegisterFile(true);
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
     * @param unitId Fetch unit id
     */
    private void fetch(int unitId) {

        String tag = String.format("FETCH(%d)", unitId);

        // Get the PC value
        int currentPcValue = this.pc.getValue();

        // Is buffer full?
        // Make sure we have enough instructions to decode later on
        if(instructionsToDecode.size() > EXECUTION_UNITS_NUM) {
            // We reached memory that isn't instructions
            Utilities.log(tag, "Instruction buffer full. Skipping.");
            return;
        }

        // Encoded instruction we will try to fetch fom the memory
        EncodedInstruction currentEncodedInstruction;

        // Try to get the instruction from memory
        try {
            currentEncodedInstruction = (EncodedInstruction) this.mainMemory.getFromMemory(currentPcValue);

            Utilities.log(tag, "Fetched " + currentEncodedInstruction.getEncodedInstruction()
                    + " at address " + Integer.toHexString(currentPcValue));
        } catch (ClassCastException ex) {
            // We reached memory that isn't instructions
            Utilities.log(tag, "nothing to do");
            return;
        }

        // Increment PC
        this.pc.setValue(currentPcValue + 0x4);
        Utilities.log("\tIncremented PC to " + Integer.toHexString(this.pc.getValue()));

        instructionsToDecode.addLast(currentEncodedInstruction);
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
        for(List<DecodedInstruction> buffer : this.instructionsToExecute) {
            for (DecodedInstruction instruction : buffer) {

                Integer destinationRegister = instruction.getDestinationRegisterNumber();

                // Instruction doesn't write back anything, so no need to worry
                if (destinationRegister == null) {
                    continue;
                }

                // Check if some instruction is writing back to our source registers
                if ((sourceRegister1 != null && sourceRegister1 == destinationRegister)
                        || (sourceRegister2 != null && sourceRegister2 == destinationRegister)) {

                    // Stall, we need to wait for the result
                    this.instructionsToDecode.addFirst(currentEncodedInstruction);
                    Utilities.log("DECODE", "Can't decode, there's dependency in "
                            + currentEncodedInstruction.getEncodedInstruction());
                    return false;
                }
            }
        }

        for(List<DecodedInstruction> buffer : this.instructionsToWriteBack) {
            for (DecodedInstruction instruction : buffer) {

                Integer destinationRegister = instruction.getDestinationRegisterNumber();

                // Instruction doesn't write back anything, so no need to worry
                if (destinationRegister == null) {
                    continue;
                }

                // Check if some instruction is writing back to our source registers
                if ((sourceRegister1 != null && sourceRegister1 == destinationRegister)
                        || (sourceRegister2 != null && sourceRegister2 == destinationRegister)) {

                    // Stall, we need to wait for the result
                    this.instructionsToDecode.addFirst(currentEncodedInstruction);
                    Utilities.log("DECODE", "Can't decode, there's dependency in "
                            + currentEncodedInstruction.getEncodedInstruction());
                    return false;
                }
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

    public void dumpRegisterFile(boolean dirtyOnly) {

        // Dump registers
        final RegisterFile registerFile = this.getRegisterFile();

        if(dirtyOnly) {
            System.out.println("Registers that changed:");
        } else {
            System.out.println("Register file dump: ");
        }

        for(int i = 0; i < registerFile.getCount(); i++) {
            Register register = registerFile.getRegister(i);

            if(!register.getDirty() && dirtyOnly) {
                continue;
            }

            int value = register.getValue();
            System.out.print("R" + String.format("%02d", i) + ":\t0x" + Integer.toHexString(value).toUpperCase());

            if((i + 1) % 4 == 0) {
                System.out.print("\n");
            } else {
                System.out.print("\t\t");
            }
        }

        if(dirtyOnly && !getPc().getDirty()) {
            return;
        }

        System.out.println("PC:\t0x" + Integer.toHexString(this.getPc().getValue()).toUpperCase());
    }

    public void incrementInstructionCounter() {
        this.instructionExecutedCount += 1;
    }
}
