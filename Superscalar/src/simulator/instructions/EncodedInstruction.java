package simulator.instructions;

import simulator.RegisterFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class EncodedInstruction extends Instruction {

    final Pattern registerPattern = Pattern.compile("r\\d\\d?");
    final Pattern interValPattern = Pattern.compile("0x\\d+");

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
        this.encodedInstruction = this.encodedInstruction.replace(label, integer.toString());
    }

    /**
     * Factory of instructions
     * @return
     */
    @Override
    public DecodedInstruction decode(RegisterFile registerFile) {

        // Find the operand in the string
        Operand operand = this.parseOperand(this.encodedInstruction);

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
        }

        throw new RuntimeException("Cannot decode instruction with operand: " + operand);
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
            params[2] =  getImmediateValueFromString(intermediateValMatcher);;
        } else {
            throw new RuntimeException("Second source register or immediate should be specified");
        }

        return params;
    }

    private int getImmediateValueFromString(Matcher intermediateValMatcher) {
        // Ignore 0x
        return Integer.parseInt(intermediateValMatcher.group(0).substring(2));
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
     * @param registerFile
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
