package simulator.tests;

import org.junit.Test;
import simulator.RegisterFile;
import simulator.instructions.DecodedInstruction;
import simulator.instructions.Instruction;
import simulator.instructions.EncodedInstruction;
import simulator.instructions.Operand;

import static org.junit.Assert.*;

public class EncodedInstructionTest {

    @Test
    public void testParseAddOperand() {

        String rawInput = "ADD r0, r0, r1";
        Operand expectedOperand = Operand.ADD;

        RegisterFile regs = new RegisterFile();
        Instruction instruction = new EncodedInstruction(rawInput);
        DecodedInstruction decodedInstruction = instruction.decode(regs);
        assertEquals("ADD operand not parsed", expectedOperand, decodedInstruction.getOperand());
    }

    @Test
    public void ignoresComment() {
        String rawInput = "SUB r0, r0, r1 ; commment to ignore";
        Instruction instruction = new EncodedInstruction(rawInput);
        assertEquals("Comment not ignored", "SUB r0, r0, r1", instruction.getEncodedInstruction());
    }

    @Test
    public void instructionCanHaveLabel() {
        String instructionString = "MUL r0, r0, r1";
        String label = "label1:";

        Operand expectedOperand = Operand.MUL;

        Instruction instruction = new EncodedInstruction(instructionString, label);

        RegisterFile regs = new RegisterFile();

        DecodedInstruction decodedInstruction = instruction.decode(regs);

        assertEquals("MUL operand not parsed", expectedOperand, decodedInstruction.getOperand());
        assertEquals("Label is not assigned", "label1", instruction.getLabel());
    }
}