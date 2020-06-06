s = "ÒdÝ¾¤¤¾ÙàåÐcÝÆ¥ÌÈáÏÜ¦aã"
a = "k33p_1t_in_pl41n"
f = ""
for i in range(len(a) - 2):
	f += chr(ord(s[i]) - ord(a[i]))
f = chr(ord(s[len(a) - 2]) - ord(a[2])) + chr(ord(s[len(a) - 1]) - ord(a[3])) + f
print(f)
