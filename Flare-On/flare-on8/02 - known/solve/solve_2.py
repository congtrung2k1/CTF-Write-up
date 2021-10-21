def rol(val, n, max_bits = 8):
	return (val << n % max_bits) & (2**max_bits - 1) |\
	 ((val & (2**max_bits - 1)) >> (max_bits - n % max_bits))


enc = [0xC7, 0xC7, 0x25, 0x1D, 0x63, 0x0D, 0xF3, 0x56]
data = [0x89, 0x50, 0x4e, 0x47, 0x0D, 0x0A, 0x1A, 0x0A]
key = [0] * 8

for i in range(8):
	for j in range(32, 126):
		if rol(enc[i] ^ j, i) - i == data[i]:
			key[i] = j
			break

print(key)
print(''.join([chr(i) for i in key]))