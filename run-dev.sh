#!/bin/sh
pkill -f "java -jar -Dprofile=dev /var/app/photo-finish/app.jar"
java -jar -Dprofile=dev /var/app/photo-finish/app.jar &
exit