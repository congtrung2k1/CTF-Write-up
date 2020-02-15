cd Documents
openssl enc -aes-256-cbc -salt -in flag.txt -out flag.txt.enc -k $(cat $pass1)$pass2$pass3
steghide embed -cf doggo.jpeg -ef $pass1 
mv doggo.jpeg ~/Pictures
useradd -p '$pass2'  user
sqlite3 /opt/table.db "INSERT INTO passwords values ('1', $pass3)"
tar -zcf /opt/table.db.tar.gz /opt/table.db
rm $pass1
unset $pass2
unset $pass3
exit
