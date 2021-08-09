FROM ubuntu:20.10

RUN apt-get update
RUN export DEBIAN_FRONTEND=noninteractive

RUN apt -y install tzdata
RUN ln -fs /usr/share/zoneinfo/America/New_York /etc/localtime
RUN dpkg-reconfigure --frontend noninteractive tzdata

RUN apt -y install openjdk-16-jre leiningen net-tools libgfortran5 ffmpeg wget

WORKDIR /usr/src/app

COPY ./docker/voice-recognition .
COPY ./target/webchange.jar ./target/webchange.jar

RUN ./init.sh

ENTRYPOINT ["./docker-start.sh"]