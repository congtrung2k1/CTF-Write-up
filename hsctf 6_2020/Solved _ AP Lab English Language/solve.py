import string

s = list("1dd3|y_3tttb5g`q]^dhn3j")

def transpose():
	trans = [11,18,15,19,8,17,5,2,12,6,21,0,22,7,13,14,4,16,20,1,3,10,9]
	
	re = ['.'] * 23
	for i in range(23):
		re[trans[i]] = s[i]

	return re

def xor():
	x = [4,1,3,1,2,1,3,0,1,4,3,1,2,0,1,4,1,2,3,2,1,0,3]
	
	for i in range(23):
		s[i] = chr(ord(s[i]) ^ x[i])

for i in range(3):
	xor()
	s = transpose()

print(''.join(s))


"""
s = "1dd3|y_3tttb5g`q]^dhn3j"

trans = [11,18,15,19,8,17,5,2,12,6,21,0,22,7,13,14,4,16,20,1,3,10,9]

x = [4,1,3,1,2,1,3,0,1,4,3,1,2,0,1,4,1,2,3,2,1,0,3]

for i in range(23):
    for c in string.printable:
        ii = i
        cc = ord(c)
        for j in range(3):
            for k in range(23):
                if vt[k] == ii:
                    ii = k
                    break
            cc ^= x[ii]
            
        if chr(cc) == s[ii]:
            flag += c

print(flag)
"""