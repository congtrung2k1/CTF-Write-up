from scapy.all import *

name = 'recording.pcapng'
packet = rdpcap(name)

for i in packet:
	if b'actf' in bytes(i.payload):
		print(bytes(i.payload).split(b'actf')[1:])
		break