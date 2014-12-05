package org.mk0934.simulator.instructions;

/**
 * Instruction operands
 */
public enum Operand {
    MOV,
    ADD, SUB, MUL,

    // Memory
    LDM, STM,

    NOP,
    CMP, BGE, BGT, BEQ, JMP,

    // Vector
    VLDM,
    VSTM,
    VADD,
    VMUL
}
