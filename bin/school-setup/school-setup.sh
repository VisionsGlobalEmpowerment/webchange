#!/bin/bash
sudo apt -y install postgresql
sudo apt -y install nginx
sudo apt -y install openjdk-17-jre
./install-db.sh
./install-app.sh
./init-app.sh
./install-nginx.sh
