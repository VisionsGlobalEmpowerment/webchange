#!/bin/bash
export CLASSPATH=$CLASSPATH:/srv/www/webchange/:.
export config=/srv/www/webchange/config.edn
source ./config.sh
java -jar /srv/www/webchange/current.jar migrate
java -jar /srv/www/webchange/current.jar init-secondary $SCHOOL_ID $EMAIL $PASSWORD

