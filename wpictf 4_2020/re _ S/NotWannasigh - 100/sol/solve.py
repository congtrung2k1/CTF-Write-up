import struct

f = open('flag-gif.EnCiPhErEd','rb').read()
s = [i for i in f]

f = open('flag.gif','wb')

key = open('out.txt','r').read()
a = []
c = ''
j = 0

for i in key:
	if i == ' ':
		t = (int(c) & 0xFF) ^ s[j]

		en = struct.pack('<B', t)
		f.write(en)
		
		j += 1
		c = ''
	else:
		c += i