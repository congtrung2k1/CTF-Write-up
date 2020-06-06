import random
import itertools
import functools

P = lambda a,b:(ord(a)^ord(b)).to_bytes(1,'big')

E = ((0,3), (1,4), (0,1), (3,4), (2,3), (1,2))

B = sorted(list('[redacted - 15 chars]'))

x = lambda a,b: bin( (ord(a)-ord(b)) ^ (int('1'*10,2)) )[0] != '-'

def u(li):
 	Q=[li[i::3]for i in range(3)]
 	for i in Q:
  		while not W(i):
   			pass
 	return Q

def W(i):
	random.shuffle(i)
	a = [bool(int(bin(ord('e'))[2:][-1]))]
	return [n(a,x(i[E[j][0]],i[E[j][1]]))for j in range(len(E))][-1]

def n(g,k):
 	g[0] = g[0] and k
 	return g[0]

f = u(B)

print(B)
print(f)

a = open('input.txt','r').read()

save = [functools.reduce(P,[(((ord(a[i])&(~ord(f[j][i%5])))|((~ord(a[i]))&(ord(f[j][i%5])))).to_bytes(1,"big"))for j in range(len(f))])for i in range(len(a))]

a_1 = repr(b''.join(save))

open('output.txt','w').write(a_1)



#   if c % 100000 == 0:
#     print(i)
#     print(c)
