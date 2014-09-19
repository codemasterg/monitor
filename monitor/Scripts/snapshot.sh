#!/bin/bash
#/usr/bin/raspistill -t 1 -ex night -o $1
/usr/bin/raspistill -awb shade -ex night -br 60 -w 1024 -h 768 -q 80 -t 1 -o $1
echo "image written to $1"