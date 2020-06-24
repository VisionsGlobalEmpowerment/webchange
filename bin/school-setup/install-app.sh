#!/bin/bash
mkdir -p /srv/www/webchange/releases
cp ./run /srv/www/webchange/run
cp ./config.edn /srv/www/webchange/
cp ./current.jar /srv/www/webchange/
chmod a+x /srv/www/webchange/run

sudo  bash << EOF
cp ./webchange.service /etc/systemd/system/webchange.service
systemctl daemon-reload
systemctl enable webchange.service
EOF
