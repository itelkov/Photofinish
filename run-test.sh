#!/bin/sh
pkill -f "java -jar -Dprofile=test /var/app/photo-finish/app.jar"
java -jar -Dprofile=test /var/app/photo-finish/app.jar &
exit