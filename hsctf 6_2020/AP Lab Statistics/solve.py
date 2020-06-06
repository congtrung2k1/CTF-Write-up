import time
import string
from z3 import *

tim = time.time()

s = Solver()

enc = "qtqnhuyj{fjw{rwhswzppfnfrz|qndfktceyba"

cnt = [0] * 29
for i in enc:
	cnt[ord(i) - 97] += 1

length = len(enc)

for i in range(length):
	globals()['arr_%d' % i] = BitVec('arr_%d' % i, 8)
	s.add(And(globals()['arr_%d' % i] >= 97, globals()['arr_%d' % i] <= 125))

	globals()['guess_%d' % i] = BitVec('guess_%d' % i, 8)
	s.add(And(globals()['guess_%d' % i] >= 97, globals()['guess_%d' % i] <= 125))
	
for i in range(1, length):
	if_1 = globals()['arr_%d' % i] == ((globals()['guess_%d' % i] + (globals()['arr_%d' % (i-1)] - 97)) - 97 + 29) % 29 + 97
	if_2 = globals()['arr_%d' % i] == ((globals()['guess_%d' % i] - (globals()['arr_%d' % (i-1)] - 97)) - 97 + 29) % 29 + 97
	s.add(If(globals()['arr_%d' % (i - 1)] % 2 == 0, if_1, if_2))

s.add(globals()['guess_0'] == ord('f'))
s.add(globals()['guess_1'] == ord('l'))
s.add(globals()['guess_2'] == ord('a'))
s.add(globals()['guess_3'] == ord('g'))
s.add(globals()['guess_4'] == ord('{'))
s.add(globals()['guess_%d' % (length-1)] == ord('}'))

while s.check() == sat:
	model = s.model()
	block = []
	out_arr = ''
	out_gue = ''

	for i in range(length):
		c = globals()['arr_%d' % i]
		out_arr += chr(model[c].as_long())

		c = globals()['guess_%d' % i]
		out_gue += chr(model[c].as_long())

		block.append(c != model[c])
	s.add(Or(block))

	print(out_arr)
	print(out_gue)
	print()

	tmp = [0] * 29
	for i in out_arr:
		tmp[ord(i) - 97] += 1

	flag = True
	for i in range(29):
		if tmp[i] != cnt[i]:
			flag = False

	if flag:
		open('result.txt','a').write(out_gue + '\n')

print(time.time() - t, "seconds")