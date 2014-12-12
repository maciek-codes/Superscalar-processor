; Computer factorial of 12
start:
    mov r0, 1     ; int c = 1
    mov r1, 1     ; int fact = 1 
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
    mul r1, r1, r0      ; fact = fact * c
    add r0, r0, 1       ; c = c + 1
end:
    NOP
; Register R1
; Should contain 479001600 (x1C8CFC00)