; for( int i = 0; i < 10; i++ ) {
;   A[ i ] = B[ i ] * C[ i ];
; }
start:
    mov r0, 0x0 ; int i =0
forloop:
    cmp r5, r0, 0x40
    BGT r5, end         ; loop or exit
    
    LDM r1, r0, arrayA	; load B[i]
    LDM r2, r6, arrayA	; load B[i+1]
    LDM r3, r7, arrayA  ; load B[i+2]
    LDM r4, r8, arrayA  ; load B[i+3]

    LDM r9, r0, arrayB  ; load B[i]
    LDM r10, r6, arrayB  ; load B[i+1]
    LDM r11, r7, arrayB  ; load B[i+2]
    LDM r12, r8, arrayB  ; load B[i+3]
    
    MUL r1, r1, r9		 ; B[i] * C[i]
    MUL r2, r2, r10      ; B[i] * C[i]
    MUL r3, r3, r11      ; B[i] * C[i]
    MUL r4, r4, r12      ; B[i] * C[i]

    STM r1, r0, arrayA	; Store at A[i] => A[i] = B[i] * C[i]
    STM r1, r6, arrayA  ; Store at A[i] => A[i] = B[i] * C[i]
    STM r1, r7, arrayA  ; Store at A[i] => A[i] = B[i] * C[i]
    STM r1, r8, arrayA  ; Store at A[i] => A[i] = B[i] * C[i]

    ADD r0, r0, 0x4		; i = i + 1
    ADD r6, r0, 0x1     ; i + 1
    ADD r7, r0, 0x2     ; i + 2
    ADD r8, r0, 0x3     ; i + 3
    
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