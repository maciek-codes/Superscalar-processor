package org.mk0934.simulator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mk0934.simulator.Memory;

public class MemoryTest {

    @Test
    public void testSaveToMemory() {

        int valueToStore = 0xFF;

        Memory memory = new Memory();
        memory.saveToMemory(valueToStore, 0x0);
        memory.saveToMemory(valueToStore, 0x4);
        memory.saveToMemory(valueToStore, 0x8);
        memory.saveToMemory(valueToStore, 0xC);

        assertEquals("Value at 0x0 is not right", valueToStore, memory.getFromMemory(0x0));
        assertEquals("Value at 0x4 is not right", valueToStore, memory.getFromMemory(0x4));
        assertEquals("Value at 0x8 is not right", valueToStore, memory.getFromMemory(0x8));
        assertEquals("Value at 0xC is not right", valueToStore, memory.getFromMemory(0xC));
    }
}