#!/bin/bash
now=$(date +"%m_%d_%Y")
file="/var/log/img_$now.jpg"
/usr/bin/raspistill -t 1 -ex night -o $file
echo "image written to $file"