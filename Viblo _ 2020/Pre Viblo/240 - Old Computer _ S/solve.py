s = [ 0xd9, 0xfe, 0xc5, 0xf4, 0x9e, 
	0xc7, 0x9b, 0xdc, 0xe7, 0xd2, 
	0xf4, 0x93, 0xfe, 0xff, 0xf4, 
	0x9e, 0xde, 0xf9, 0xce, 0xe7, 0xd2]
a = 0xAB
flag = ''
for i in s:
	flag += chr(i ^ a)
print(flag)