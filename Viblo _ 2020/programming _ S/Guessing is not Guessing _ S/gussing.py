from pwn import *

re = remote('172.104.49.143', 9234)
re.recv()
re.recv()

n = 70
l,r = 0, 18446744073709551616
while n >= 0:
	m = (l + r) // 2
	print(m)
	re.sendline(str(m).encode())

	s = re.recv()
	print(s)

	if s.find(b'low') != -1:
		l = m + 1
	elif s.find(b'high') != -1:
		r = m - 1
	else:
		print(s)
		exit()
	print(l,r)

	n -= 1
