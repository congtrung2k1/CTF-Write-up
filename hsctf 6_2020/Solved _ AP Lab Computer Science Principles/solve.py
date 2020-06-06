import string

s = "inagzgkpm)Wl&Tg&io"

flag = ''

for i in range(18):
    for c in string.printable:
        t = ord(c) - i
        if chr(t + len(str(t))) == s[i]:
            flag += c
            break

print(flag)