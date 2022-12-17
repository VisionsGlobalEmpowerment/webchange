FROM ubuntu:20.04

RUN apt-get update
RUN export DEBIAN_FRONTEND=noninteractive

RUN apt-get -y install tzdata
RUN ln -fs /usr/share/zoneinfo/America/New_York /etc/localtime
RUN dpkg-reconfigure --frontend noninteractive tzdata

RUN apt-get -y install openjdk-17-jre leiningen net-tools libgfortran5 ffmpeg wget

WORKDIR /usr/src/app

COPY ./docker ./docker
COPY ./target ./target

RUN ./docker/voice-recognition-dev/init.sh

ENTRYPOINT ["./docker/voice-recognition-dev/docker-start.sh"]