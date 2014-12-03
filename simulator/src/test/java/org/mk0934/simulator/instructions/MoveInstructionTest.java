package org.mk0934.simulator.instructions;

import org.junit.Test;
import org.mk0934.simulator.Memory;
import org.mk0934.simulator.Processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for MOV instruction
 */
public class MoveInstructionTest {

    Processor processor = new Processor(new Memory());

    @Test
    public void checkIfMoveWithImmediateIsDecodedCorrectly() {

        String encodedInstructionString = "MOV r2, 0x2";

        EncodedInstruction encodedInstruction = new EncodedInstruction(encodedInstructionString);

        DecodedInstruction decodedInstruction = encodedInstruction.decode(processor);

        assertEquals(2, (int)decodedInstruction.getDestinationRegisterNumber());
        assertNull("No first register", decodedInstruction.getFirstSourceRegisterNumber());
        assertNull("No second register", decodedInstruction.getSecondSourceRegisterNumber());
    }

    @Test
    public void checkIfMoveRegisterIsDecodedCorrectly() {

        String encodedInstructionString = "MOV r2, r4";

        EncodedInstruction encodedInstruction = new EncodedInstruction(encodedInstructionString);

        DecodedInstruction decodedInstruction = encodedInstruction.decode(processor);

        assertEquals(2, (int)decodedInstruction.getDestinationRegisterNumber());
        assertEquals(4, (int)decodedInstruction.getFirstSourceRegisterNumber());
        assertNull("No second register", decodedInstruction.getSecondSourceRegisterNumber());
    }

    @Test
    public void checkIfMoveRegisterIsDecodedCorrectlyWhenDecimalImm() {

        String encodedInstructionString = "MOV r2, 20";

        EncodedInstruction encodedInstruction = new EncodedInstruction(encodedInstructionString);

        DecodedInstruction decodedInstruction = encodedInstruction.decode(processor);

        assertEquals(2, (int)decodedInstruction.getDestinationRegisterNumber());
        assertNull("No first register", decodedInstruction.getFirstSourceRegisterNumber());
        assertNull("No second register", decodedInstruction.getSecondSourceRegisterNumber());


        decodedInstruction.execute(processor);
        decodedInstruction.writeBack(processor);

        assertEquals("Value in register R2 should be updated", 20, processor.getRegisterFile().getRegister(2).getValue());
    }

    @Test
    public void checkIfMoveChangesRegisterValue() {

        String encodedInstructionString = "MOV r0, 0xF5";
        String encodedInstructionString2 = "MOV r1, r0";

        EncodedInstruction encodedInstruction = new EncodedInstruction(encodedInstructionString);
        DecodedInstruction decodedInstruction = encodedInstruction.decode(processor);
        decodedInstruction.execute(processor);
        decodedInstruction.writeBack(processor);

        assertEquals("Value in register R0 should be updated", 0xF5, processor.getRegisterFile().getRegister(0).getValue());

        encodedInstruction = new EncodedInstruction(encodedInstructionString2);
        decodedInstruction = encodedInstruction.decode(processor);
        decodedInstruction.execute(processor);
        decodedInstruction.writeBack(processor);

        assertEquals("Value in register R1 should be updated", 0xF5, processor.getRegisterFile().getRegister(1).getValue());
    }
}
