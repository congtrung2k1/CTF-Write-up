import string

s = [ 0x58, 0x00, 0x00, 0x00, 0x41, 0x00, 0x00, 0x00, 0x50, 0x00, 
  0x00, 0x00, 0x52, 0x00, 0x00, 0x00, 0x4D, 0x00, 0x00, 0x00, 
  0x58, 0x00, 0x00, 0x00, 0x43, 0x00, 0x00, 0x00, 0x53, 0x00, 
  0x00, 0x00, 0x42, 0x00, 0x00, 0x00, 0x43, 0x00, 0x00, 0x00, 
  0x45, 0x00, 0x00, 0x00, 0x44, 0x00, 0x00, 0x00, 0x49, 0x00, 
  0x00, 0x00, 0x53, 0x00, 0x00, 0x00, 0x42, 0x00, 0x00, 0x00, 
  0x56, 0x00, 0x00, 0x00, 0x58, 0x00, 0x00, 0x00, 0x49, 0x00, 
  0x00, 0x00, 0x53, 0x00, 0x00, 0x00, 0x58, 0x00, 0x00, 0x00, 
  0x57, 0x00, 0x00, 0x00, 0x45, 0x00, 0x00, 0x00, 0x52, 0x00, 
  0x00, 0x00, 0x4A, 0x00, 0x00, 0x00, 0x52, 0x00, 0x00, 0x00, 
  0x57, 0x00, 0x00, 0x00, 0x53, 0x00, 0x00, 0x00, 0x5A, 0x00, 
  0x00, 0x00, 0x41, 0x00, 0x00, 0x00, 0x52, 0x00, 0x00, 0x00, 
  0x53, 0x00, 0x00, 0x00, 0x51 ]

de = [0] * 0x10
j = 0
for i in range(0x20):
	if i & 1 == 0:
		de[0xF - j] = chr(s[i * 4])
		j += 1

de = ''.join(de)
print(de)
#SASRRWSXBIEBCMPX


x = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\]^_`{|}~"
s = "0A8/@7.?6-KBSJARIZQHYPGXOFWNEVMDULCTMDULCTKBSJARIZQHYPGXOFWNEV5,=4+<3*;2):1(9>5,=4+<MDULCTKBSJ"

flag = ''
for i in de:
	flag += x[s.index(i)]
print(flag)
#c1cffqcnbgsbyuln