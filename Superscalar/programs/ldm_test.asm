MOV r0, 0x0
LDM r1, r0, arrayA	; load A[0]
ADD r0, r0, 0x8
LDM r2, r0, arrayA	; load A[2]
ADD r0, r0, 0x4
LDM r3, r0, arrayA	; load A[3]
end:
SVC 0
arrayA: ; array A
0x1     ; A[0]
0x2     ; A[1]
0x3     ; A[2]
0x4     ; A[3]
0x5     ; A[4]