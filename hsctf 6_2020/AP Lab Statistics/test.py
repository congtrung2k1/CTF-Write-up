enc = "qtqnhuyj{fjw{rwhswzppfnfrz|qndfktceyba"
print(len(enc))
cnt = [1, 1, 1, 1, 1, 4, 0, 2, 0, 2, 1, 0, 0, 3, 0, 2, 3, 2, 1, 2, 1, 0, 3, 0, 2, 2, 2, 1, 0]
#for i in enc:
#	cnt[ord(i) - 97] += 1

for i,j in zip(range(29), cnt):
	if j > 0:
		print(chr(i + 97), j, (float(j) / len(enc)) * 100)

s = 'coxdx'

for i in range(97, 97 + 26):
	if cnt[i - 97] == 0:
		continue

	arr = [0] * 5

	arr[0] = i
	for j in range(1,5):
		if arr[j - 1] % 2 == 0:
			arr[j] = ord(s[j]) + arr[j - 1] - 97
		else:
			arr[j] = ord(s[j]) - arr[j - 1] - 97

		arr[j] = (arr[j] - 97 + 29) % 29 + 97

	def swap():
		for i in range(1,5):
			if arr[i - 1] <= arr[i]:
				arr[i - 1], arr[i] = arr[i], arr[i - 1]

	print(chr(i))
	print([chr(i) for i in arr])

	swap()
	swap()
	print([chr(i) for i in arr])

	co = True
	for i in range(5):
		if enc[i] != chr(arr[i]):
			co = False
	if co:
		print('------------------------')
		print(chr(i))
		print([chr(i) for i in arr])		
		print('------------------------')

