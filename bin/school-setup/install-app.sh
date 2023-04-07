#!/bin/bash
sudo chmod 777 /srv/
mkdir -p /srv/www/webchange
mkdir -p /srv/www/webchange/public
sudo chown www-data:www-data /srv/www/webchange/public
mkdir -p /srv/www/webchange/log
sudo chown www-data:www-data /srv/www/webchange/log

cp ./run /srv/www/webchange/run
cp ./config.edn /srv/www/webchange/
cp ./current.jar /srv/www/webchange/
sudo chown www-data:www-data /srv/www/webchange/current.jar
chmod a+x /srv/www/webchange/run

sudo  bash << EOF
cp ./webchange.service /etc/systemd/system/webchange.service
systemctl daemon-reload
systemctl enable webchange.service
EOF
