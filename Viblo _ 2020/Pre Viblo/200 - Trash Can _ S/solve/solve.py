data = list(open('trash_can','rb').read())

save = []
for i in range(0x1316, 0x2BFC1, 0x202):
	save.append(data[i])

index = [0] * 0xB1

index[0xB0] = 0x104
index[0xAC] = 0x0E
index[0xA8] = 0x7B 
index[0xA4] = 0x0D8
index[0xA0] = 0x3E 
index[0x9C] = 0x0B6
index[0x98] = 0x0AA
index[0x94] = 0x0AB
index[0x90] = 0x88
index[0x8C] = 0x48 
index[0x88] = 0x67 
index[0x84] = 0x116
index[0x80] = 0x142
index[0x7C] = 0x21 
index[0x78] = 0x133
index[0x74] = 0x0AE
index[0x70] = 0x116
index[0x6C] = 0x0F6
index[0x68] = 0x16
index[0x64] = 0x58 
index[0x60] = 0x116
index[0x5C] = 0x0AA
index[0x58] = 0x32 
index[0x54] = 0x10E
index[0x50] = 0x8C
index[0x4C] = 0x58 
index[0x48] = 0x116
index[0x44] = 0x93
index[0x40] = 0x9B
index[0x3C] = 0x116
index[0x38] = 0x58 
index[0x34] = 0x0B0
index[0x30] = 0x145
index[0x2C] = 0x8C
index[0x28] = 0x116
index[0x24] = 0x0AE
index[0x20] = 0x6F 
index[0x1C] = 0x8B
index[0x18] = 0x43 
index[0x14] = 0x0B0
index[0x10] = 0x93
index[0xC] = 0x112

flag = ''
for i in range(0xB0, 0xB, -4):
	flag += chr(save[index[i]])
	
print(flag)