; Bubble sort
start:
    MOV r0, 0x1
repeat:
    MOV r2, 0x4         ; i = 1
    MOV r1, 0x0         ; swapped  = false
forloop:
    CMP r3, r2, 0x44
    BGE r3, endfor      ; end for loop if i > len(array)
    SUB r4, r2, 0x4     ; i-1 
    LDM r8, r2, array   ; a =  array[i]
    LDM r9, r4, array   ; b = array[i-i]
    ADD r2, r2, 0x4     ; i++
    CMP r7, r8, r9
    BGE r7, forloop
swap:
    STM r9, r2, array       ; array[i] = b;
    STM r8, r4, array       ; array[i-i] = a; 
    MOV r1, 0x1             ; swapped = true
    JMP forloop
endfor:
    CMP r3, r1, 0x1         ; if(swapped == true)
    BEQ r3, repeat          ; then goto repeat
end:
    NOP
array:
10
2
5
4
12
6
9
105
9
40
1
23
3
16
18
23
10