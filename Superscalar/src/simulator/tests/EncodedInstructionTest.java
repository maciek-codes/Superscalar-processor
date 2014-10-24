package simulator.tests;

import org.junit.Test;
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

        Instruction instruction = new EncodedInstruction(rawInput);
        DecodedInstruction decodedInstruction = instruction.decode();
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
        DecodedInstruction decodedInstruction = instruction.decode();

        assertEquals("MUL operand not parsed", expectedOperand, decodedInstruction.getOperand());
        assertEquals("Label is not assigned", "label1", instruction.getLabel());
    }
}