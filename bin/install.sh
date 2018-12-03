#!/bin/bash
mkdir -p /srv/www/webchange/releases
cp ./run /srv/www/webchange/run
chmod a+x /srv/www/webchange/run
cp ./webchange.service /etc/systemd/system/webchange.service

systemctl daemon-reload
systemctl enable webchange.service
