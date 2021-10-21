import os

direc = 'antioch'

list = []
for i in os.scandir(direc):
	if i.is_dir():
		for sub in os.scandir(direc + '/' + i.name):
			if sub.name == 'json':
				path = direc + '/' + i.name + '/' + 'json'
				
				f = open(path, 'r').read()
				if 'author' not in f:
					break

				author = f.split('author":"')[1].split('","')[0]
				print(author, i.name)



