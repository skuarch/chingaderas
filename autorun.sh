#!/bin/bash

(sleep 12; yakuake) & 
(sleep 350; kdesudo -u root fstrim /) &

SERVICE='chrome'
 
if ps ax | grep -v grep | grep $SERVICE > /dev/null
then
    echo "$SERVICE service running, everything is fine"
else
    (sleep 30; google-chrome) & 
fi

SERVICE='thunderbird'
 
if ps ax | grep -v grep | grep $SERVICE > /dev/null
then
    echo "$SERVICE service running, everything is fine"
else
    (sleep 300; thunderbird) & 
fi
