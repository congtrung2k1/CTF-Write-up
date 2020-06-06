import time
from z3 import *

out = [1, 18, 21, 18, 73, 20, 65, 8, 8, 4, 24, 24, 9, 18, 29, 21, 3, 21, 14, 6, 18, 83, 2, 26, 86, 83, 5, 20, 27, 28, 85, 67, 5, 17, 2, 7, 12, 11, 17, 0, 2, 20, 12, 26, 26, 30, 15, 44, 15, 31, 0, 12, 46, 8, 28, 23, 0, 11, 3, 25, 14, 0, 65]

#a_1 = [[[flag[z + y + x] for x in range(3)] for y in range(0, 21, 3)] for z in range(0, 63, 21)]

s = Solver()

for i in range(63):
	globals()['x%d' % i] = BitVec('x%d' % i, 8)
	s.add(Or(And(globals()['x%d' % i] >= 32, globals()['x%d' % i] <= 47), And(globals()['x%d' % i] >= 58, globals()['x%d' % i] <= 64), And(globals()['x%d' % i] >= 91, globals()['x%d' % i] <= 126)))
	#'abcdefghijklmnopqrstuvwxyz !"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~'

for i in range(7):
	globals()['key_%d' % i] = BitVec('key_%d' % i, 8)
	s.add(globals()['key_%d' % i] >= 97)
	s.add(globals()['key_%d' % i] <= 122)	
	#abcdefghijklmnopqrstuvwxyz

c = 0
for k in range(0, 63, 21):
	for j in range(0, 21, 3):
		for i in range(j + k, j + k + 3):

			s.add( globals()['x%d' % i] ^ globals()['key_%d' % (j // 3)] ^ globals()['key_%d' % (i - j - k)] ^ globals()['key_%d' % (k // 21)] == out[c])

			c += 1


for vt in range(63 - 6):

	s.push()

	s.add(globals()['x%d' % vt] == ord('t'))
	s.add(globals()['x%d' % (vt + 1)] == ord('j'))
	s.add(globals()['x%d' % (vt + 2)] == ord('c'))
	s.add(globals()['x%d' % (vt + 3)] == ord('t'))
	s.add(globals()['x%d' % (vt + 4)] == ord('f'))
	s.add(globals()['x%d' % (vt + 5)] == ord('{'))
	
	while s.check() == sat:
		model = s.model()
		block = []
		flag = ''
		key = ''

		for i in range(0,63):
			c = globals()['x%d' % i]
			
			flag += chr(model[c].as_long())

			block.append(c != model[c])

		a = 'abcdefghijklmnopqrstuvwxyz'
		p = ' !"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~'
		if set(flag).issubset(set(a + p)) and flag.count('tjctf{') == 1 and flag.count('}') == 1 and flag.count(' ') == 5:
			print(flag)

		s.add(Or(block))
	
	s.pop()