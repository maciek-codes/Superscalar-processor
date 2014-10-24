package simulator.instructions;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class EncodedInstruction extends Instruction {

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
        }

        throw new NotImplementedException();
    }
}
