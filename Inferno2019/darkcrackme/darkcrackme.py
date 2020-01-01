def find_pos(re):
	for i in range(17):
		num = i
		ele = 3
		temp = []
		for j in range(4):
			temp.append(1)
		while (num > 0):
			if num & 1:
				temp[ele] = 0
			else:
				temp[ele] = 1
			ele -= 1
			num //= 2

		che = True
		for j in range(4):
			if (re[j] != temp[j]):
				che = False
				break
		if che:
			return i

use = "1_4m_th3_wh1t3r0s3"
pas = ""

def_string_1 = "ADGJLQETUOZCBM10"
def_string_2 = "sfhkwryipxvn5238"

ans = ["{0:b}".format(ord(i)) for i in use]

for i in range(len(ans)):
	while (len(ans[i]) < 8):
		ans[i] = '0' + ans[i]

for i,s in enumerate(ans):
	print(i, use[i], s)

	arr_1 = []
	arr_2 = []
	for j,x in enumerate(s):
		if (j & 1):
			arr_2.append(int(x))
		else:
			arr_1.append(int(x))

	pos_1 = find_pos(arr_1)
	pos_2 = find_pos(arr_2)

	print(arr_1, pos_1)
	print(arr_2, pos_2)

	pas += def_string_1[pos_1] + def_string_2[pos_2]
	
print(pas)

