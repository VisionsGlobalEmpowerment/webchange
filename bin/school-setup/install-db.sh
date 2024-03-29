#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd)"
echo $DIR
sudo -u postgres -i bash << EOF
psql  --command="CREATE USER webchange WITH PASSWORD 'webchange';"
createdb --owner=webchange webchange
psql webchange --command='CREATE EXTENSION IF NOT EXISTS "uuid-ossp";'
psql webchange < $DIR/dump-secondary.sql
EOF

