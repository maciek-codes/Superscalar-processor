package simulator.instructions;

import simulator.RegisterFile;

import java.util.Objects;

/**
 * Created by Maciej Kumorek on 10/16/2014.
 */
public abstract class Instruction {

    private String label;

    protected String encodedInstruction;

    public Instruction() {
        this.label = "";
    }

    public Instruction(String label) {
        this.label = parseLabel(label);
    }

    private final String parseLabel(String label) {

        return label.replace(':', '\0').trim();
    }

    /**
     * Parse string to get the operand
     * @param instructionString
     * @return
     */
    protected Operand parseOperand(String instructionString) {

        String string = instructionString.toLowerCase();

        if (string.contains("add")) {
            return Operand.ADD;
        } else if(string.startsWith("sub ")) {
            return Operand.SUB;
        } else if(string.startsWith("mul ")) {
            return Operand.MUL;
        } else if(string.startsWith("bl ")) {
            return Operand.BL;
        } else if(string.startsWith("blt")) {
            return Operand.BLT;
        } else if(string.startsWith("mov ")) {
            return Operand.MOV;
        } else if(string.startsWith("ldm ")) {
            return Operand.LDM;
        } else if(string.startsWith("stm ")) {
            return Operand.STM;
        } else if(string.startsWith("jmp ")) {
            return Operand.JMP;
        } else if(string.startsWith("svc ")) {
            return Operand.SVC;
        }

        throw new RuntimeException("Unknown operand in string " + string);
    }



    public String getLabel() {
        return this.label;
    }

    public final String getEncodedInstruction() {
        return this.encodedInstruction;
    }

    public abstract DecodedInstruction decode(RegisterFile registerFile);
}
