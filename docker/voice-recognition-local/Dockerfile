FROM ubuntu:20.10

RUN apt-get update
RUN export DEBIAN_FRONTEND=noninteractive

RUN apt -y install tzdata
RUN ln -fs /usr/share/zoneinfo/America/New_York /etc/localtime
RUN dpkg-reconfigure --frontend noninteractive tzdata

RUN apt -y install openjdk-8-jre leiningen net-tools libgfortran5 ffmpeg ruby-sass nodejs npm wget

WORKDIR /usr/src

RUN wget https://alphacephei.com/vosk/models/vosk-model-en-us-daanzu-20200905-lgraph.zip
RUN unzip vosk-model-en-us-daanzu-20200905-lgraph.zip
RUN mv "/usr/src/vosk-model-en-us-daanzu-20200905-lgraph" "/usr/src/model"
RUN wget http://host.webchange.com/dev-gems/libvosk_jni.so

WORKDIR /usr/src/app

ENTRYPOINT export config=/usr/src/app/config.edn && java -Djava.library.path=/usr/src/ -jar target/webchange.jar recognition-worker