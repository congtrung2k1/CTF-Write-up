#./taking_off 3 9 2 chicken
a = [ 0x5A, 0x46, 0x4F, 0x4B, 0x59, 0x4F, 0x0A, 0x4D, 0x43, 0x5C, 
  0x4F, 0x0A, 0x4C, 0x46, 0x4B, 0x4D, 0x2A ]

pas = ''

for i in a:
	pas += chr(i ^ 0x2A)
print(pas)
