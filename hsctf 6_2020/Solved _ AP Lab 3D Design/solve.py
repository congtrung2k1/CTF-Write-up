s = "11010110110001101110011011001110110001100011101110101100101110011100100000111011011011000111100011111010010101100111100000111001101111101000000100101100001010011001110011001100010011101110111011001100"

def StringToarr():
	tmp = []
	v = 0
	for i in range(5):
		t_1 = []
		for j in range(5):
			t_2 = []
			for k in range(8):
				t_2.append(ord(s[v]))
				v += 1
			t_1.append(t_2)
		tmp.append(t_1)
	return tmp

arr = StringToarr()

def shuffle_1():
	for i in range(8):
		if i % 2 == 1:
			for j in range(5):
				k = arr[j][3][i]
				arr[j][3][i] = arr[j][1][i]
				arr[j][1][i] = arr[j][2][i]
				arr[j][2][i] = arr[j][4][i]
				arr[j][4][i] = arr[j][0][i]
				arr[j][0][i] = k
		else:
			for j in range(5):
				k = arr[j][2][i]
				arr[j][2][i] = arr[j][0][i]
				arr[j][0][i] = arr[j][1][i]
				arr[j][1][i] = arr[j][4][i]
				arr[j][4][i] = arr[j][3][i]
				arr[j][3][i] = k

def shuffle_2():
	hi = [1, 0, 0, 1, 0, 1, 1, 1]
	for i in range(5):
		for j in range(5):
			if (i * 5 + i * j) % 2 == 1:
				for a in range(8):
					arr[i][j][a] ^= hi[a]

shuffle_2()
shuffle_1()

flag = ''
for i in range(5):
	for j in range(5):
		tmp = '0b'
		for k in range(7,-1,-1):
			tmp += chr(arr[i][j][k])
		flag += chr(int(tmp, 2))

print(flag)