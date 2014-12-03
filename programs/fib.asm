; Compute n'th fibbonaci number
start:
    mov r0, 0      ; int i = 0
    mov r1, 0      ; prev1 = 0 - fib_1
    mov r2, 1      ; prev2 = 1 - fib_2
    mov r3, 0 	   ; savePrev1 = 0;
forloop:
    cmp r4, r0, 43      ; if i < n
    bge r4, end         ;   then end
    mov r3, r1          ; savePrev1 = prev1
    mov r1, r2		    ; prev1 = prev2
    add r0, r0, 1       ; i = i +
    add r2, r2, r3
    jmp forloop         ; next loop iteration
end:
    nop

; 43rd fibbonaci number is 433494437 = 0x19D699A5