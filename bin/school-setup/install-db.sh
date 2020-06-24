#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd)"
echo $DIR
sudo -u postgres -i bash << EOF
psql  --command="CREATE USER webchange WITH PASSWORD 'webchange';"
psql webchange --command='CREATE EXTENSION IF NOT EXISTS "uuid-ossp";'
createdb --owner=webchange webchange
psql webchange < $DIR/dump-secondary.sql
EOF
source config.sh
#java -jar webchange.jar migrate
#java -jar webchange.jar init-secondary $SCHOOL_ID $EMAIL $PASSWORD
