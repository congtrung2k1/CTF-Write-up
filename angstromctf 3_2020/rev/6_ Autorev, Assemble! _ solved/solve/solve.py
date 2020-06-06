f = open('autorev_assemble','rb').read()

flag = ['*'] * 256

de = f[0x594 : 0x59F]
#48 89 7D F8 48 8B 45 F8 48 83 C0

de_0 = f[0x79CC : 0x79CC + 3]
#48 8D 3D 

#begin possition in main
pos = 0x79CB
while pos < 0x8938:
	pos = f.find(de_0, pos + 1)
	#print(hex(pos))
	#find next block to call function

	if pos == -1:
		break

	tmp = pos + 8
	#position of funtion to call
	#print(hex(tmp))
	#print([hex(j) for j in f[tmp : tmp + 2]])
	#print('0xFFFFFFFFFFFF' + hex(f[tmp + 1])[2:].zfill(2) + hex(f[tmp])[2:].zfill(2))
	
	p = (tmp + 4 + int('0xFFFFFFFFFFFF' + hex(f[tmp + 1])[2:].zfill(2) + hex(f[tmp])[2:].zfill(2), 16)) & 0xFFFF
	#print(hex(p))
	#position of that function in script

	if (f[p + 0xF] != 0):
		fin = p + 0xF
		flag[f[fin]] = f[fin + 5]
		#print([hex(j) for j in f[fin: fin + 6]])
		#1st format of function

	else:
		fin = p + 0xE
		flag[f[fin]] = f[fin + 8]
		#print([hex(j) for j in f[fin: fin + 9]])
		#2nd format of function

	#print()

print(''.join([chr(i) if type(i) == int else i for i in flag]))

"""
print(flag)
for i in flag:
	if type(i) == int:
		print(chr(i), end='')
	else:
		print(i, end='')
"""