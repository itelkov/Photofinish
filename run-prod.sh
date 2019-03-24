#!/bin/sh
pkill -f "java -jar -Dprofile=production /var/app/photo-finish/app.jar"
java -jar -Dprofile=production /var/app/photo-finish/app.jar &
exit