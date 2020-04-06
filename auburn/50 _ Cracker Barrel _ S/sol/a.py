#starwar
#secret message this is

s = list('z!!b6~wn&`')
x = ''
for i in s:
	x += chr((ord(i) ^ 0x14) - 2)
print(''.join(x))

#auctf{w3lc0m3_to_R3_1021}