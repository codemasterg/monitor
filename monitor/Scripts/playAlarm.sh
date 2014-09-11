#!/bin/bash
/usr/bin/seq 12 | /usr/bin/xargs -I INDEX /usr/bin/aplay -N /usr/share/scratch/Media/Sounds/Effects/Siren.wav
