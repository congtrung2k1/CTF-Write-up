s = "ÐdØÓ§åÍaèÒÁ¡"
a = "h1_th3r3_1ts_m3"
f = ""
for i in range(len(s)):
	f += chr(ord(s[i]) - ord(a[i]))
print(f)