de = [  1557693418,
  4016472256,
  2949410950,
  2117777535,
  2110093114,
  941654069,
  72795125 ]

change = 0x1337BEEF

def shift(a, b):
	return (a >> b) & 1
def bytes_shifted():
	global change
	v0 = shift(change, 0)
	v1 = shift(change, 2) ^ v0
	v2 = shift(change, 3) ^ v1
	v3 = shift(change, 5)
	change = change >> 1
	change |= (v2 ^ v3) << 31
	return change

ans = ''
for i in range(7):
	v3 = bytes_shifted()
	tmp = str(hex((v3 + de[i]) & 0xFFFFFFFF))
	ans += ''.join([chr(int(tmp[i:i+2],16)) for i in range(len(tmp)-2,0,-2)])

print(ans)