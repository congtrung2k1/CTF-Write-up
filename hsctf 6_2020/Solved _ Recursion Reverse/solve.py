import string
import numpy as np

s = "I$N]=6YiVwC"

num = 0

flag = ''


def pickNum(ar):
	global num
	num = np.uint32(num + (ar * (ar + 1)) // 2)

	if num % 2 == 0:
		return num
	else:
		num = pickNum(num)
	return num


for i in range(12):

	tmp = []
	for c in string.printable:
		num = 1

		t = chr((ord(c) + pickNum(i + 1)) % 127)

		if t == s[11-i]:
			flag += c
			
print(flag)