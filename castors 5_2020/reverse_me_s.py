import binascii
import string

s = list(binascii.unhexlify('64 35 68 35 64 37 33 7a 38 6b 33 37 6b 72 67 7a'.replace(' ','').encode()).decode())

flag = ''
for i in range(len(s)):
	for c in string.printable:

		t = ord(c) + 2
		if t > 96 and t <= 122:
			t = (t - 87) % 26 + 97

		if t == ord(s[i]):
			flag += c

print(flag)
