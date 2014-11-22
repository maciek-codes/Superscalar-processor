package org.mk0934.simulator;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.mk0934.simulator.Memory;
import org.mk0934.simulator.Processor;
import org.mk0934.simulator.RegisterFile;
import org.mk0934.simulator.instructions.DecodedInstruction;
import org.mk0934.simulator.instructions.Instruction;
import org.mk0934.simulator.instructions.EncodedInstruction;
import org.mk0934.simulator.instructions.Operand;

public class EncodedInstructionTest {


    /**
    *   Test case for add
    */
    @Test
    public void testParseAddOperand() {

        String rawInput = "ADD r0, r0, r1";
        Operand expectedOperand = Operand.ADD;

        Processor proc = new Processor(new Memory());
        Instruction instruction = new EncodedInstruction(rawInput);
        DecodedInstruction decodedInstruction = instruction.decode(proc);
        assertEquals("ADD operand not parsed", expectedOperand, decodedInstruction.getOperand());
    }

    /**
    *   Test case for ignoring comments in assmebly file
    */
    @Test
    public void ignoresComment() {
        String rawInput = "SUB r0, r0, r1 ; commment to ignore";
        Instruction instruction = new EncodedInstruction(rawInput);
        assertEquals("Comment not ignored", "SUB r0, r0, r1", instruction.getEncodedInstruction());
    }

    /**
    *   Test case for ignoring comments in assmebly file
    */
    @Test
    public void instructionCanHaveLabel() {
        String instructionString = "MUL r0, r0, r1";
        String label = "label1:";

        Operand expectedOperand = Operand.MUL;

        Instruction instruction = new EncodedInstruction(instructionString, label);

        Processor proc = new Processor(new Memory());

        DecodedInstruction decodedInstruction = instruction.decode(proc);

        assertEquals("MUL operand not parsed", expectedOperand, decodedInstruction.getOperand());
        assertEquals("Label is not assigned", "label1", instruction.getLabel());
    }
}