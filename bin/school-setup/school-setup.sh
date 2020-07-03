#!/bin/bash
sudo apt -y install postgresql
sudo apt -y install nginx
sudo apt -y install openjdk-8-jre
./install-db.sh
./install-app.sh
./init-app.sh
install-nginx.sh
