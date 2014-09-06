#!/bin/bash
/usr/bin/raspistill -t 1 -ex night -o $1
echo "image written to $1"