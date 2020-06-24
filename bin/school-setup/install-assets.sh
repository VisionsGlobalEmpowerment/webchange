#!/bin/bash
unzip raw.zip
mv raw /srv/www/webchange/public/
export config=/srv/www/webchange/config.edn
java -jar /srv/www/webchange/current.jar calc-asset-hash
chmod 555 /srv/www/webchange/public/raw/ -R
