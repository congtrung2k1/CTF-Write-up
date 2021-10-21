from itertools import permutations

"""
3 : DFWEyEW
4 : PXopvM
13: BGgsuhn

7 : newaui
10: HwdwAZ
11: SLdkv
"""

data = [   0x96, 0x25, 0xA4, 0xA9, 0xA3, 0x96, 0x9A, 0x90, 0x9F, 0xAF, 
		  0xE5, 0x38, 0xF9, 0x81, 0x9E, 0x16, 0xF9, 0xCB, 0xE4, 0xA4, 
		  0x87, 0x8F, 0x8F, 0xBA, 0xD2, 0x9D, 0xA7, 0xD1, 0xFC, 0xA3, 
		  0xA8]

key = ['newaui','HwdwAZ','SLdkv']
fis = ['DFWEyEW','PXopvM','BGgsuhn']

perk = permutations(key)
perf = permutations(fis)

a = list(perk)
b = list(perf)

for i in a:
	for j in b:
		v1 = [ord(x) for x in ''.join(j)]

		v9 = [ord(x) for x in ''.join(i)]

		v13 = [x for x in data]

		for x in range(31):
			v13[x] ^= v1[x % len(v1)] 
			v13[x] = (v13[x] - v9[x % 17]) & 0xFF

		res = ''.join([chr(x) for x in v13])
		if '@flare-on.com' in res:
			print(f'---> {i}, {j} \n---> {res}')


"""
for i in a:
	for j in b:
		print('\n--->', i, j)

		v1 = ''.join(j)
		v1 = [ord(x) for x in v1]
		print('[v1] :', v1)

		v9 = ''.join(i)
		v9 = [ord(x) for x in v9]
		print('[v9] :', v9)

		v13 = [x for x in data]

		for x in range(31):
			v13[x] ^= v1[x % len(v1)] 
			v13[x] = (v13[x] - v9[x % 17]) & 0xFF

		ret = ''.join([chr(x) for x in v13])
		print('--->', ret)
"""
