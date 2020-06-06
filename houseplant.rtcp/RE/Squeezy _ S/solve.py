import base64

def woah(s1,s2):
    return ''.join(chr(ord(a) ^ ord(b)) for a,b in zip(s1,s2))

key = "meownyameownyameownyameownyameownya"

s = base64.b64decode(b'HxEMBxUAURg6I0QILT4UVRolMQFRHzokRBcmAygNXhkqWBw=', altchars=None).decode()

print(woah(s, key))