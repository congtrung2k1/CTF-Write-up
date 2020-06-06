import base64
from itertools import cycle

f = (base64.b64decode('NSYDUhoVWQ8SQVcOAAYRFQkORA4FQVMDQQ5fQhUEWUYMDl4MHA==')).decode('ascii')
x = 'TG20{'
print(''.join([chr(ord(f[i]) ^ ord(x[i])) for i in range(5)]))