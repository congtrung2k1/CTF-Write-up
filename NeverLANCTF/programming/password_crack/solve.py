import hashlib

c = 'White Yellow Blue Red Green Black Brown Azure Ivory Teal Silver Purple Navy \
 blue Pea green Gray  Orange Maroon Charcoal Aquamarine Coral Fuchsia Wheat Lime \
 Crimson Khaki Hot pink Magenta Olden Plum Olive Cyan'.lower().split(' ')

y = [str(i).zfill(4) for i in range(0,10000)]

n = 'purvesta N30 ZestyFE viking s7a73farm BashNinja NULL_b0n3s'.lower().split(' ')

for i in c:
	for j in y:
		for k in n:
			pla = i + '-' + j + '-' + k
			res = hashlib.md5(pla.encode())

			if res.hexdigest() == "267530778aa6585019c98985eeda255f":
				print(pla)
				exit()


