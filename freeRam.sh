#!/bin/bash
AUTHORIZED_USER="root"
if [ $USER != $AUTHORIZED_USER ]; then
echo "Este script debe ser ejecutado por el usuario $AUTHORIZED_USER" 1>&2
exit 1 
else 
echo
echo 
echo "SIN LIMPIAR" 
echo
echo 
free -m 
echo "LIMPIANDO..." 
echo
echo
echo 3 > /proc/sys/vm/drop_caches; 
echo "LIMPIO" 
free -m
fi
