FROM ubuntu:22.04

RUN apt-get update
RUN export DEBIAN_FRONTEND=noninteractive

RUN ln -fs /usr/share/zoneinfo/America/New_York /etc/localtime
RUN apt-get install -y tzdata 
RUN dpkg-reconfigure --frontend noninteractive tzdata

RUN apt-get -y install curl wget gzip nginx openjdk-17-jre postgresql-client

curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install
chmod +x install
./install

WORKDIR /app
