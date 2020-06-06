import base64
def xor(s1,s2):
    return ''.join(chr(ord(a) ^ ord(b)) for a,b in zip(s1,s2))
x = base64.b64decode(b'ExMcGQAABzohNQ0TRQwtPidYAS8gXg4kAkcYISwOUQYS', altchars=None).decode()
key = "promortyusvatofacidpromortyusvato"
print(xor(x,key))