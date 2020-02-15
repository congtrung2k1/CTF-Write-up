from scapy.all import *

name = 'telnet.pcap'
packet = rdpcap(name)

for i in packet:
	if b'flag%7B' in bytes(i.payload):
		print(bytes(i.payload).split(b'flag%7B')[1:])
		break
	if b'flag{' in bytes(i.payload):
		print(bytes(i.payload).split(b'flag{')[1:])
		break