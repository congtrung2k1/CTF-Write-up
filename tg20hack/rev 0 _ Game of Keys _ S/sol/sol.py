import base64
from itertools import cycle

class myGame:

    def __init__(self, xdim=4, ydim=4):
        self.x = xdim
        self.y = ydim
        self.matrix = []
        for i in range(self.x):
            row = []
            for j in range(self.y):
                row.append(0)

            self.matrix.append(row)
        words = []
        with open('wordlist.txt') as (f):
            for line in f:
                words.append(line.strip())

            for i in range(self.x):
                for j in range(self.y):
                    self.matrix[j][i] = words[(i + j)]

    def make_keys(self, *args, **kwargs):
        keyArray = []
        keyArray.append(self.matrix[args[0]][args[1]])
        keyArray.append(self.matrix[args[2]][args[3]])
        key = ''
        for k in keyArray:
            key = key.strip() + str(k).strip()
        return key

    def checkdata(self, key):
        f = base64.b64decode('NSYDUhoVWQ8SQVcOAAYRFQkORA4FQVMDQQ5fQhUEWUYMDl4MHA==')
        data = f.decode('ascii')
        c = ''.join((chr(ord(c) ^ ord(k)) for c, k in zip(data, cycle(key))))
        return c

if __name__ == '__main__':
    mgame = myGame(25, 25)

    for i in range(25):
        for j in range(25):
            if (mgame.matrix[i][j] == 'aa1b'):
                for k in range(25):
                    for l in range(25):
                        data = mgame.make_keys(int(i), int(j), int(k), int(l))
                        if (data[:5] == 'aa1ba'):
                            print(mgame.checkdata(data))

#TG20{this flag should be on teh moon}
