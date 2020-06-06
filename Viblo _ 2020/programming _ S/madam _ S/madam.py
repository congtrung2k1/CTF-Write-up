f = open('password.txt')

pas = ''
maxx = 0
for ch in f:
	for i in range(len(ch) - 1):
		l = 1
		while i - l >= 0 and i + l < len(ch) and ch[i-l] == ch[i+l]:
			l += 1
		if 2 * l - 1 > maxx:
			maxx = 2 * l - 1
			pas = ch[i - l + 1 : i + l]

		if ch[i] == ch[i + 1]:
			l = 1
			while i - l >= 0 and i + 1 + l < len(ch) and ch[i-l] == ch[i+1+l]: 
				l += 1
			if 2 * l - 2 > maxx:
				maxx = 2 * l - 2
				pas = ch[i - l + 1 : i + l + 1]
print(pas)
