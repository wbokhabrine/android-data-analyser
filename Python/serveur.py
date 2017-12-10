#!/usr/bin/python
# coding: utf-8

import socket
import time

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(('', 15550))
sock.listen(5)
client, address = sock.accept()
print("{} connected".format( address ))

x =  float(client.recv(1024)[2:]) + 1
print(x)
start = int( (time.time() * 1000))
n = 0.0+x
while n < (x + 10):
    time.sleep(2)
    print("SENDING : {}".format(n))
    client.send(str(n) + ';' + str(int((time.time()) * 1000) - start) + '\0')
    n += 1.0

time.sleep(1)
print("Close")
client.close()
sock.close()