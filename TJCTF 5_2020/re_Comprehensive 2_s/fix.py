m = 'tjctf{' + ' ' * 5 + 'a' * 51 + '}'
n = 'b' * 7

a = 'abcdefghijklmnopqrstuvwxyz'
p = ' !"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~'

assert len(m) == 63 and set(m).issubset(set(a + p))
assert len(n) == 7  and set(n).issubset(set(a))
assert m.count('tjctf{') == 1 and m.count('}') == 1 and m.count(' ') == 5

a_1 = []

for k in range(0, len(m), 21):

	temp = []

	for j in range (0, 21, 3):

		temp_2 = []

		for i in range(j + k, j + k + 3):

			temp_2.append( ord(m[i]) ^ ord(n[j // 3]) ^ ord(n[i - j - k]) ^ ord(n[k // 21]) )

		temp.append(temp_2)

	a_1.append(temp)

print(a_1)

flag = []

for z in a_1: 
	for y in z:
		for x in y:
			flag.append(x)


ss = ''.join(str(flag))

final = ss[1:-1]

print(final)