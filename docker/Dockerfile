FROM ubuntu:22.04

RUN apt-get update
RUN export DEBIAN_FRONTEND=noninteractive

RUN ln -fs /usr/share/zoneinfo/America/New_York /etc/localtime
RUN apt-get install -y tzdata 
RUN dpkg-reconfigure --frontend noninteractive tzdata

RUN apt-get -y install curl wget gzip nginx openjdk-17-jdk postgresql-client
RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && apt-get install -y nodejs

RUN wget https://github.com/sass/dart-sass/releases/download/1.57.1/dart-sass-1.57.1-linux-x64.tar.gz
RUN tar -xf dart-sass-1.57.1-linux-x64.tar.gz
RUN cp ./dart-sass/sass /usr/local/bin/sass

RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
RUN cp ./lein /usr/local/bin/lein
RUN chmod a+x /usr/local/bin/lein
RUN lein

curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install
chmod +x install
./install

WORKDIR /app

CMD lein with-profile +cljs,+cljs-test repl :headless :host "0.0.0.0" :port 45555