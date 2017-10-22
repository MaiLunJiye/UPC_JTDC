#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import json
import sys

if len(sys.argv) < 4:
    print('usage: %s iplistPath wlan0 ipgroup' % __file__)
    print('example:')
    print('>> %s iplist_MP.json wlan0 server' % __file__)
    exit(0)

[JSONpath, dev, ipgroup] = sys.argv[1:4]

ipstruct = None
with open(JSONpath, 'r') as f:
    iplist = f.read()
    ipstruct = json.loads(iplist)
    f.close()

if ipgroup not in ipstruct:
    print('can not find ipgroup %s' % ipgroup)
    exit(1)

netmask = "255.255.255.0"
index = 0

for ip in ipstruct[ipgroup]:
    os.popen("ifconfig %s:%s %s netmask %s up" % (dev, index, ip, netmask))
    index += 1
    print(ip)
