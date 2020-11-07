FROM ubuntu:20.04

COPY ./webchange /webchange
COPY ./docker-start.sh /webchange/docker-start.sh

RUN apt-get update
RUN export DEBIAN_FRONTEND=noninteractive

RUN ln -fs /usr/share/zoneinfo/America/New_York /etc/localtime
RUN apt-get install -y tzdata
RUN dpkg-reconfigure --frontend noninteractive tzdata

RUN apt -y install nginx
RUN apt -y install openjdk-8-jre
RUN apt -y install leiningen
RUN apt -y install npm
RUN apt -y install postgresql-client
RUN apt -y install ruby-sass

RUN cp ./webchange/bin/school-setup/config.edn /webchange/
RUN echo '{:provided {:env {:database-url "jdbc:postgresql://db:5432/webchange?user=webchange&password=webchange" :public-dir   "resources/public" :port   "3000" :upload-dir   "resources/public/upload" :dev? true }}}' > /webchange/profiles.clj

RUN cd /webchange/ && lein sass once
RUN cd /webchange/ && lein cljsbuild once sw
RUN cd /webchange/ && lein cljsbuild once min

CMD /bin/sh /webchange/docker-start.sh

