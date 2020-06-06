- get .git file, save as .pack

- According to the https://git-scm.com/docs/pack-format#_pack_pack_files_have_the_following_format, 4 first byte is the signature : {'P', 'A', 'C', 'K'}.

- Delete all b4.

- Open with cat a.pack | git unpack-objects

- cd ./git/objects

- git show <hash>

	Make sure to use foldername+bodyname as the hash
	
	./git/objects/87/b1647544bdcf0e896e08080ec84bb8b57cccc8d0
	-> git show 87b1647544bdcf0e896e08080ec84bb8b57cccc8d0

- git show 87872f28963e229e8271e0fab6a557a1e5fb5131:flag.jpg > flag.jpg
