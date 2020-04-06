import string

sec = 'aQLpavpKQcCVpfcg'

flag = ''

for i in range(len(sec)):
	for j in string.printable:
		if ((ord(j) * 8 + 0x13) % 0x3d + 0x41 == ord(sec[i])):
			flag += j
			break

print(flag)
#75y"72"b5eak"0eG


#auctf{that_w@s_2_ezy_29302}
