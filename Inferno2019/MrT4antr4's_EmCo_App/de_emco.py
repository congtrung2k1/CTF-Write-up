from PIL import Image

def de_encrypt(file_path):
	im = Image.open(file_path).convert('RGB')

	pixels = list(im.getdata())

	for i in range(100):
		pixels.append((125,0,0))

	line = ''
	for i in pixels:
		tmp = '{0:b}'.format(i[2])
		tmp = '0' * (8 - len(tmp)) + tmp
		line += tmp

	ori = b''
	for i in line:
		if (i == '1'):
			ori += bytes([0]) + bytes([0]) + bytes([0])
		else:
			ori += bytes([255]) + bytes([255]) + bytes([255])

	#make beauty
	for i in range(100):
		ori += bytes([0]) + bytes([0]) + bytes([0])

	im2 = Image.frombytes('RGB', (200, 200), ori)
	im2.save("de_emco.png", "PNG")

de_encrypt("encrypted.png")