from z3 import *

finish = 'CCHJEHMKCFKJCEOLFOJLMOJJBDN@H@BAODMJHFCJMOOKMOOOOOAOFOGI@@@@@@@@'

out = [ord('@')] * 64

s = Solver()
for i in range(32):
	globals()['b%d' % i] = BitVec('b%d' % i, 8)

	for j in range(8):
		out[(j << 3) + ((i + j) & 7)] |= (globals()['b%d' % i] & (1 << (j & 7))) >> (j & 7) << ((i >> 3) & 7)

for i in range(64):
	s.add(out[i] == ord(finish[i]))

while s.check() == sat:
	model = s.model()
	block = []
	ans = ''
	for i in range(32):
		c = globals()['b%d' % i]
		ans += chr(model[c].as_long())
		block.append(c != model[c])
	s.add(Or(block))
	print(ans)