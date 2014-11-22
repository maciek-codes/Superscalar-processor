; Bubble sort
start:
    MOV r0, 0x1
repeat:
    MOV r1, 0x0         ; swapped  = false
    MOV r2, 0x1         ; i = 1
forloop:
    CMP r3, r2, 0x9
    BGE r3, endfor      ; end for loop if i > len(array)
    SUB r4, r2, 0x1     ; i-1
    MUL r5, r2, 0x4     ; offset for array[i]
    MUL r6, r4, 0x4     ; offset for arrray[i-1]
    LDM r8, r5, array   ; load array[i]
    LDM r9, r6, array   ; load array[i-i]
    CMP r7, r8, r9
    BGE r7, next
swap:
    STM r9, r5, array
    STM r8, r6, array
    MOV r1, 0x1
next:
    ADD r2, r2, 0x1
    JMP forloop
endfor:
    CMP r3, r1, 0x1         ; if(swap == true)
    BEQ r3, repeat          ;   goto repeat
end:
    SVC 0
array:
10
2
5
4
6
9
9
1
3