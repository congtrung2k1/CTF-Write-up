Don't be an ida dog or ida cat or ida mouse or ......

Given file: pro

Solve:

use Bruceforce.sh to bruceforce the key:

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

$ ./bruceforce.sh > output.txt && cat ouput | grep -B 1 "flag"

with each key, use it to trace the flag.

Flag: BambooFox{dyn4mic_1s_4ls0_gr34t}
