package simulator.instructions;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    public DecodedInstruction decode() {

        Operand operand = this.parseOperand(this.encodedInstruction);

        if(operand == Operand.SVC) {
            return new SvcInstruction(operand);
        } else if(operand == Operand.ADD) {
            return new AddInstruction(operand);
        } else if(operand == Operand.MUL) {
            return new MultiplyInstruction(operand);
        } else if(operand == Operand.MOV) {

            // Get destination register

            Matcher matcher = registerPattern.matcher(getEncodedInstruction());
            String desinationRegisterName, sourceRegisterName;

            if(matcher.find()) {
                desinationRegisterName = matcher.group(0);
            } else {
                throw new RuntimeException("Destination register not specified in instruction: "
                        + this.getEncodedInstruction());
            }

            if(matcher.find()) {
                sourceRegisterName = matcher.group(0);
                return new MoveInstruction(desinationRegisterName, sourceRegisterName);

            } else {
                Matcher intermediateValMatcher = interValPattern.matcher(this.getEncodedInstruction());

                if(intermediateValMatcher.find()) {
                    // Ignore 0x
                    int value = Integer.parseInt(intermediateValMatcher.group(0).substring(2));
                    return new MoveInstruction(desinationRegisterName, value);
                }
            }

            throw new RuntimeException("Cannot decode MOV instruction: " + this.getEncodedInstruction());
        }

        throw new RuntimeException("Cannot decode instruction with operand: " + operand);
    }
}
