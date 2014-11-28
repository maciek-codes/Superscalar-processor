start:
    MOV r0, 0x1
    CMP r1, r0, 0x0
    BGE r1, L2
L1:
    MOV r2, 0x4
    JMP end
L2:
    MOV r0, 0x2
    CMP r1, r0, 0x2
    BGT r1, L3
    BGE r1, L1 
L3: 
    MOV r5, 0x5
end:
    NOP