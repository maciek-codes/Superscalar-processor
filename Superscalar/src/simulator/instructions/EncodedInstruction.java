package simulator.instructions;

import simulator.Memory;
import simulator.Processor;
import simulator.RegisterFile;
import simulator.StatusRegister;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class EncodedInstruction extends Instruction {

    final Pattern registerPattern = Pattern.compile("r\\d\\d?");
    final Pattern interValPattern = Pattern.compile("0x\\w+");

    public EncodedInstruction(String instructionString) {

        this.encodedInstruction = this.parseEncodedInstruction(instructionString);
    }

    public EncodedInstruction(String instructionString, String label) {
        super(label);
        this.encodedInstruction = this.parseEncodedInstruction(instructionString);
    }

    protected String parseEncodedInstruction(String instructionString) {
        if(!instructionString.contains(";")) {
            return instructionString;
        }

        int indexOfSemicolon = instructionString.indexOf(';');
        return instructionString.substring(0, indexOfSemicolon).trim();
    }

    public void replaceLabelWithAddress(String label, Integer integer) {
        this.encodedInstruction = this.encodedInstruction.replace(label, "0x" + Integer.toHexString(integer));
    }

    /**
     * Factory of instructions
     * @return instance of Decoded instruction
     */
    @Override
    public DecodedInstruction decode(Processor processor) {

        // Find the operand in the string
        Operand operand = this.parseOperand(this.encodedInstruction);

        // Get register file
        RegisterFile registerFile = processor.getRegisterFile();

        // Choose decoding logic for an operation
        if(operand == Operand.SVC) {

            return new SvcInstruction(operand);
        } else if(operand == Operand.ADD) {
            // Decode ADD
            return this.decodeAdd(registerFile);
        } else if(operand == Operand.MUL) {
            // Decode MUL
            return this.decodeMul(registerFile);
        } else if(operand == Operand.MOV) {
            // Decode MOV
            return this.decodeMov(registerFile);
        } else if(operand == Operand.SUB) {
            // Decode SUB
            return this.decodeSub(registerFile);
        } else if(operand == Operand.LDM) {
            // Decode LDM - memory load
            return this.decodeLoadMemory(registerFile);
        } else if(operand == Operand.STM) {
            // Decode STM - memory store
            return this.decodeStoreMemory(registerFile);
        } else if(operand == Operand.CMP) {
            // Decode CMP
            return this.decodeCmp(registerFile);
        } else if(operand == Operand.BGE) {
            // Decode BGE - Branch - greater - equal
            return this.decodeBranchGreaterEqual(processor.getStatusRegister());
        } else if(operand == Operand.JMP) {
            // Decode JMP - Jump instruction
            return this.decodeJmp();
        }

        throw new RuntimeException("Cannot decode instruction with operand: " + operand);
    }

    /**
     * Decode JMP
     *
     * JMP takes only one argument, absolute address to jump to
     * @return JumpInstructon instance
     */
    private DecodedInstruction decodeJmp() {
        int address = this.getImmediateParam();
        return new JumpInstruction(address);
    }

    private DecodedInstruction decodeBranchGreaterEqual(StatusRegister statusRegister) {

        int address = this.getImmediateParam();

        StatusRegister.Status status = statusRegister.getStatus();

        return new BranchGreaterEqualInstruction(address, status);
    }

    /**
     * Find single immediate argument value
     * @return immediate value parsed
     */
    private int getImmediateParam() {

        // Try to get immediate value
        Matcher intermediateValMatcher = interValPattern.matcher(this.getEncodedInstruction());

        if(!intermediateValMatcher.find()) {
            throw new RuntimeException("Second source register or immediate should be specified");
        }

        return this.getImmediateValueFromString(intermediateValMatcher);
    }

    private int[] getTreeParams(RegisterFile registerFile) {

        int[] params = new int[3];

        // Get destination register
        Matcher matcher = registerPattern.matcher(this.getEncodedInstruction());
        String destinationRegisterName, sourceRegisterName;

        if(matcher.find()) {
            destinationRegisterName = matcher.group(0);
            params[0] = this.getRegisterNumberFromString(destinationRegisterName);
        } else {
            // Destination must be specified
            throw new RuntimeException("Destination register not specified in instruction: "
                    + this.getEncodedInstruction());
        }

        // Get First source register
        if(matcher.find()) {
            sourceRegisterName = matcher.group(0);
            int registerNumber = this.getRegisterNumberFromString(sourceRegisterName);
            int valueInRegisterOne = registerFile.getRegister(registerNumber).getValue();
            params[1] = valueInRegisterOne;
        } else {
            throw new RuntimeException("First argument should be source register");
        }

        // Get second register or immediate value
        if(matcher.find()) {
            sourceRegisterName = matcher.group(0);
            int registerNumber = this.getRegisterNumberFromString(sourceRegisterName);
            int valueInRegister = registerFile.getRegister(registerNumber).getValue();
            params[2] = valueInRegister;
            return params;
        }

        // Try to get immediate value
        Matcher intermediateValMatcher = interValPattern.matcher(this.getEncodedInstruction());

        if(intermediateValMatcher.find()) {
            params[2] =  getImmediateValueFromString(intermediateValMatcher);
        } else {
            throw new RuntimeException("Second source register or immediate should be specified");
        }

        return params;
    }

    private int getImmediateValueFromString(Matcher intermediateValMatcher) {
        String hexNumberStr = intermediateValMatcher.group(0);
        return Memory.tryParse(hexNumberStr);
    }

    private int getRegisterNumberFromString(String registerName) {
        if(registerName.startsWith("r")) {
            return Integer.parseInt(registerName.substring(1));
        }

        return Integer.parseInt(registerName);
    }

    /**
     * Decode ADD instruction.
     *
     * ADD must specify desination register
     * ADD can take two registers
     * or one register and intermediate value
     * @param registerFile processor's register file
     */
    private DecodedInstruction decodeAdd(RegisterFile registerFile) {

        int[] args = this.getTreeParams(registerFile);
        return new AddInstruction(args);
    }

    /**
     * Decode MUL instruction
     *
     * MUL must specify destination register
     * MUL can take two registers or a register and immediate
     * @param registerFile
     * @return
     */
    private DecodedInstruction decodeMul(RegisterFile registerFile) {
        int[] args = this.getTreeParams(registerFile);
        return new MultiplyInstruction(args);
    }

    /**
     * Decode SUB instruction
     *
     * SUB must specify destination register
     * SUB can take two registers or a register and immediate
     * @param registerFile
     * @return
     */
    private DecodedInstruction decodeSub(RegisterFile registerFile) {
        int[] args = this.getTreeParams(registerFile);
        return new SubInstruction(args);
    }

    /**
     * Decode LDM instruction
     *
     * LDM must specify destination register
     * LDM can take two registers or a register and immediate
     * @param registerFile
     * @return
     */
    private DecodedInstruction decodeLoadMemory(RegisterFile registerFile) {
        int[] args = this.getTreeParams(registerFile);
        return new LoadMemoryInstruction(args);
    }

    /**
     * Decode LDM instruction
     *
     * LDM must specify destination register
     * LDM can take two registers or a register and immediate
     * @param registerFile
     * @return
     */
    private DecodedInstruction decodeStoreMemory(RegisterFile registerFile) {
        int[] args = this.getTreeParams(registerFile);

        // Actually first register is not destination, so we need to get its value rather than number
        int registerNumber = args[0];
        args[0] = registerFile.getRegister(registerNumber).getValue();

        return new StoreMemoryInstruction(args);
    }

    private DecodedInstruction decodeCmp(RegisterFile registerFile) {

        int[] args = this.getTwoArgValues(registerFile);

        return new CompareInstruction(args);
    }

    private int[] getTwoArgValues(RegisterFile registerFile) {

        // Try to get immediate value
        Matcher intermediateValMatcher = interValPattern.matcher(this.getEncodedInstruction());

        int[] args = new int[2];

        // Get destination register
        Matcher matcher = registerPattern.matcher(this.getEncodedInstruction());
        String registerName;
        int registerNumber = -1;

        if(matcher.find()) {
            registerName = matcher.group(0);
            registerNumber = this.getRegisterNumberFromString(registerName);
            args[0] = registerFile.getRegister(registerNumber).getValue();
        } else if(intermediateValMatcher.find()) {
            int immediateValue = getImmediateValueFromString(intermediateValMatcher);
            args[0] = immediateValue;
        } else {
            // LHS must be specified
            throw new RuntimeException("LHS register not specified in instruction: "
                    + this.getEncodedInstruction());
        }

        // Get second register or immediate value
        if(matcher.find()) {
            registerName = matcher.group(0);
            registerNumber = this.getRegisterNumberFromString(registerName);
            args[1] = registerFile.getRegister(registerNumber).getValue();
        } else if(intermediateValMatcher.find()) {
            int immediateValue = getImmediateValueFromString(intermediateValMatcher);
            args[1] = immediateValue;
        } else {
            // RHS must be specified
            throw new RuntimeException("RHS register not specified in instruction: "
                    + this.getEncodedInstruction());
        }

        return args;
    }

    private DecodedInstruction decodeMov(RegisterFile registerFile) {

        // Get destination register
        Matcher matcher = registerPattern.matcher(this.getEncodedInstruction());
        String destinationRegisterName, sourceRegisterName;
        int destinationRegisterNumber = -1;

        if(matcher.find()) {
            destinationRegisterName = matcher.group(0);
            destinationRegisterNumber = this.getRegisterNumberFromString(destinationRegisterName);
        } else {

            // Destination must be specified
            throw new RuntimeException("Destination register not specified in instruction: "
                    + this.getEncodedInstruction());
        }

        // Get second register or immediate value
        if(matcher.find()) {
            sourceRegisterName = matcher.group(0);
            int registerNumber = this.getRegisterNumberFromString(sourceRegisterName);
            int valueInRegister = registerFile.getRegister(registerNumber).getValue();
            return new MoveInstruction(destinationRegisterNumber, valueInRegister);
        }

        // Try to get immediate value
        Matcher intermediateValMatcher = interValPattern.matcher(this.getEncodedInstruction());

        if(intermediateValMatcher.find()) {
            int immediateValue = getImmediateValueFromString(intermediateValMatcher);
            return new MoveInstruction(destinationRegisterNumber, immediateValue);
        }

        // Else give up
        throw new RuntimeException("Cannot decode MOV instruction: " + this.getEncodedInstruction());
    }
}
