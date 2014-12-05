; for( int i = 0; i < 10; i++ ) {
;   A[ i ] = B[ i ] * C[ i ];
; }
start:
    MOV r0, 0x0 ; int i =0
    MOV r1, 0x0 ; int offset = 0'
forloop:
    CMP r3, r0, 0xF
    BGE r3, end         ; loop or exit
    VLDM r4, r1, arrayA	; load B[i]
    VLDM r8, r1, arrayB	; load C[i]
    VMUL r4, r4, r8		; B[i] * C[i]
    VSTM r4, r1, arrayA	; Store at A[i] => A[i] = B[i] * C[i]
    ADD r0, r0, 0x4 	; i += 1
    ADD r1, r1, 0x10    ; offset += 16
    JMP forloop			;
end:
    NOP
arrayA:
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
    0x2
arrayB:
    0x0
    0x1
    0x2
    0x3
    0x4
    0x5
    0x6
    0x7
    0x8
    0x9
    0xA
    0xB
    0xC
    0xD
    0xE
    0xF