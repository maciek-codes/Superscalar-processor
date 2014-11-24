; Computer factorial of 4
start:
    MOV r0, 0x6     ; int n = 6
    MOV r1, 0x1     ; int c = 1
    MOV r2, 0x1     ; int fact = 1
forloop:
    CMP r3, r1, r0      ; check if c <= n
    BGT r3, end         ; finish if not
    MUL r2, r2, r1      ; fact = fact * c
    ADD r1, r1, 0x1     ; c++
    JMP forloop         ; next loop iteration
end:
    NOP
; Should print 720 (0x2D0)