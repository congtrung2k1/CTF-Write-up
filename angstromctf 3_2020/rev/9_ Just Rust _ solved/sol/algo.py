out = [ord('@')] * 64

finish = '@O@O@OHOEDDIIBBMKCLLHHAAH@@BBDDHOOOOOOOOOOOOOOOO@@@@HH@@@@@@@@@@'
inp = "012345678901234567890123456789wq"

for i in range(32):
	#c_269 = i >> 3

	for j in range(8):

		# a = inp[i]
		# dil = 1 << (j & 7)
		# a = a & dil

		# dil_1 = a >> (j & 7)
		# c_269 = c_269 & 7
		# dil_2 = dil_1 << (c_269 & 7)

		# ax = j << 3
		# out[ax + (i + j) & 7] |= dil_2

		out[(j << 3) + ((i + j) & 7)] |= (ord(inp[i]) & (1 << (j & 7))) >> (j & 7) << ((i >> 3) & 7)

s = ''.join([chr(i) for i in out])
print('\n'.join([s[i * 8 : i * 8 + 8] for i in range(8)]))

#@O@O@OHOEDDIIBBMKCLLHHAAH@@BBDDHOOOOOOOOOOOOOOOO@@@@HH@@@@@@@@@@


