#!/usr/bin/expect -f
for {set i 0x00} {$i <= 0xFF} {incr i} {
	spawn ./pro

	expect "First give me your password: " {
		send "98416\r"
	}

	expect "Second give me your key: " {
		send "$i\r"
	}
	
	expect "Then verify your flag: " {
		send "_____________________________\r" 
	}
}
