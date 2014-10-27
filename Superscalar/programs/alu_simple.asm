MOV r0, 0x2     ; int i =2
MOV r1, 0x3     ; int j =3
ADD r1, r0, r1  ; int j = i + j = 5
ADD r1, r1, 0x1 ; j++ = 6
SUB r2, r1, r0  ; int l = j - i = 4
SUB r2, r2, 0x1   ; l-- => 3
MUL r3, r1, 0x3 ; int m = j * 3  = 18
MUL r4, r3, r2  ; int n = m * l = 54
SVC 0           ; EOF