#!/bin/bash
sudo -u postgres -i
source bin/school-setup/config.sh
psql  --command="CREATE USER webchange WITH PASSWORD 'webchange';"
psql  --command="CREATE EXTENSION IF NOT EXISTS 'uuid-ossp';"
createdb --owner=webchange webchange
psql webchange1 < dump-secondary.sql
exit

/srv/www/webchange/run init-secondary $SCHOOL_ID $EMAIL $PASSWORD