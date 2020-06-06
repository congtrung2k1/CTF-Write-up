from pwn import *

def cal(n):
	return (n * (2* n + 1)*(n+1) // 12) +n  +1 - n * (n + 1 ) // 4

re = remote('172.104.49.143', 9723)
re.recv()

while True:
	s = re.recv()
	print(s)

	n = int(s.decode().split(' ')[2].split(')')[0])
	n = cal(n)
	re.sendline(str(n).encode())

	s = re.recv()
	print(s)
	print()


#Flag{STFAH_and_eat_watermelon}