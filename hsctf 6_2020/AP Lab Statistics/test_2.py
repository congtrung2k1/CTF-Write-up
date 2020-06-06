enc = "qtqnhuyj{fjw{rwhswzppfnfrz|qndfktceyba"
endd = 5
s = [126] * 6

for i in range(26):
	#1st: random
	arr = [126] * 6
	arr[0] = i + 97
	"""
	print(chr(i + 97))
	print('s ---> ',end='')
	print([chr(ite) for ite in s])
	print('arr -> ',end='')
	print([chr(ite) for ite in arr])
	stop = input()
"""
	for j in range(26):
		for k in range(26):
			#2nd,3rd on flag
			s[1] = j + 97
			if arr[0] % 2 == 0:
				arr[1] = j + arr[0] 
			else:
				arr[1] = j - arr[0]
			arr[1] = (arr[1] - 97 + 29) % 29 + 97

			s[2] = k + 97
			if arr[1] % 2 == 0:
				arr[2] = k + arr[1] 
			else:
				arr[2] = k - arr[1]
			arr[2] = (arr[2] - 97 + 29) % 29 + 97
			savesave = arr[1]

			if chr(arr[2]) == 'q':

					for l in range(26):
						for m in range(26):
							for n in range(1):
								s[0], s[1], s[2], s[3], s[4], s[5] = 126, j+97, k+97, l+97, m+97, n+97
								arr[0], arr[1], arr[2] = i+97, savesave, ord('q')

								for u in range(3,endd):
									if arr[u - 1] % 2 == 0:
										arr[u] = s[u] + arr[u - 1] 
									else:
										arr[u] = s[u] - arr[u - 1]
									arr[u] = (arr[u] - 97 + 29) % 29 + 97

								def swap():
									for ite in range(1,endd):
										if arr[ite - 1] <= arr[ite]:
											arr[ite - 1], arr[ite] = arr[ite], arr[ite - 1]

								#print(chr(i + 97))
								#print('s ---> ',end='')
								#print([chr(ite) for ite in s])
								#print('arr -> ',end='')
								
								tmp = ([chr(ite) for ite in arr])

								swap()
								swap()
								#print('arr -> ',end='')
								#print([chr(ite) for ite in arr])
								#stop = input()

								co = True
								for ite in range(endd):
									if enc[ite] != chr(arr[ite]):
										co = False
								if co:
									print('------------------------')
									print(chr(i + 97))
									print('s ---> ',end='')
									print([chr(ite) for ite in s])
									print('arr -> ',end='')
									print(tmp)
									print('arr -> ',end='')
									print([chr(ite) for ite in arr])
									print('------------------------')