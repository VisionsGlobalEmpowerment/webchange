FROM ubuntu:20.10

RUN apt-get update
RUN export DEBIAN_FRONTEND=noninteractive

RUN apt -y install tzdata
RUN ln -fs /usr/share/zoneinfo/America/New_York /etc/localtime
RUN dpkg-reconfigure --frontend noninteractive tzdata

RUN apt -y install openjdk-8-jre leiningen net-tools libgfortran5 ffmpeg ruby-sass nodejs npm wget

WORKDIR /usr/src/app

COPY . .

RUN ./docker/voice-recognition-dev/init.sh

ENTRYPOINT ["./docker/voice-recognition-dev/docker-start.sh"]