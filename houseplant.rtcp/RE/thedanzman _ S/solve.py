import base64
import codecs

def nope(s1,s2):
	return ''.join(chr(ord(a) ^ ord(b)) for a,b in zip(s1,s2))

def wow(x):
	return x[::-1]

userinput = "thelonnaomabieaaahamacuaaamaithectf"
key = "nyameowpurrpurrnyanyapurrpurrnyanya"
key = codecs.encode(key, "rot_13")

a = nope(key,userinput)
print('a',a,type(a))
b = str.encode(a)
print('b',b,type(b))
c = base64.b64encode(b, altchars=None)
print('c',c,type(c))
c = str(c)
print('c',c,type(c))
d = codecs.encode(c, 'rot_13')
print('d',d,type(d))
result = wow(d)
print('result',result,type(result))
print()

#result = "'=ZkXipjPiLIXRpIYTpQHpjSQkxIIFbQCK1FR3DuJZxtPAtkR'o"
d = wow(result)
print('d',d,type(d))
c = codecs.decode(d, 'rot_13')
print('c',c,type(c))
c = c[2:-1].encode()
print('c',c,type(c))
b = base64.b64decode(c)
print('b',b,type(b))
a = b.decode()
print(nope(key,a))



key = "nyameowpurrpurrnyanyapurrpurrnyanya"
key = codecs.encode(key, "rot_13")


result = "'=ZkXipjPiLIXRpIYTpQHpjSQkxIIFbQCK1FR3DuJZxtPAtkR'o"
d = wow(result)
c = codecs.decode(d, 'rot_13')
c = c[2:-1].encode()
b = base64.b64decode(c, altchars=None)
a = b.decode(encoding='utf-8')
f = nope(key, a)

print(f)


