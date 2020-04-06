s = 'welcome_to_the_jungle!'
len_inp = len(s)

f = [0] * len_inp

v5 = 0
index = 0
while (v5 < len_inp):
	if v5 != 0:
		f[index] = s[v5]
		index += 1
	
	v5 += index + 2
	
l_20 = 0
l_21 = 0
i_3 = 0
while (l_20 < len_inp):
	i_3 = l_20

	if l_20 == 0:
		i_3 = 1

	if l_20 != 0:
		l_21 += 1

	f[index] = s[l_20]
	index += 1
	l_20 = i_3 + 2 + l_21

i_4 = 1
i_3 = 0
i_2 = 0
while i_4 < len_inp:
	i_2 = i_3 + 1

	if i_4 < 7:
		i_2 = i_3

	f[index] = s[i_4]
	index += 1

	i_4 += 3 + i_2
	i_3 = i_2

l_20 = 3
l_21 = 3
v_4 = len_inp - 3

while index < len_inp:
	tmp = v_4 - 3

	if l_20 != 0:
		tmp = v_4

	f[index] = s[tmp]
	index += 1

	v_4 = tmp - 1
	i_3 = l_21 - 1

	if l_20 != 0:
		i_3 = l_20

	i_4 = l_21 - 1
	if l_20 != 0:
		i_4 = l_21

	l_21 = i_4
	l_20 = i_3 - 1

print(''.join(f))
#lmo_ewce_j!eo_tulgneht

#