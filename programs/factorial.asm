; Computer factorial of 12
start:
    mov r0, 1     ; int c = 1
    mov r1, 1     ; int fact = 1
forloop:
    cmp r2, r0, 12      ; if c <= n
    bgt r2, end         ;   then goto end 
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    jmp forloop         ; goto forloop
end:
    NOP
; Register R1
; Should contain 479001600 (x1C8CFC00)