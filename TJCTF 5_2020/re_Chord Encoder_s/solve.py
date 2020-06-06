l = {'A':'1', 'B':'2', 'C':'3', 'D':'4', 'E':'5', 'F':'6', 'G':'7'}

pattern = {'000': 'g', '001' : 'G', '010' : 'd', '020' : 'D', '0100' : 'e', '0112' : 'A', '0122' : 'a', '0200' : 'E', '1002': 'c', '1011': 'f', '1121' : 'F', '1012': 'C', '2100': 'b', '2110': 'B'}

enc = '1121112111211002112101121121001001210000101221121011200102000110120200101100100111211011001020020010111012011202001011112110121121011211211002112110020200101111210112020010111121010112102001121100211211011020020001010'

temp = ['.'] * 58

c = 0

def find(save):
	flag = ''

	for i in range(0, len(save), 2):
		t = '0x'
		t += l[save[i]] if save[i] in l else save[i]
		t += l[save[i + 1]] if save[i + 1] in l else save[i + 1]
		flag += chr(int(t, 16))

	print(flag)

def calc(i,c):

	if i == len(enc):
		find(temp)
		return

	for key in pattern:
		if i + len(key) - 1 < len(enc) and enc[i : i + len(key)] == key:
			temp[c] = pattern[key]
			calc(i + len(key), c + 1)

calc(0,0)

