## Configuring voice recognition for local development

###Configs

Put following params to your profiles.clj 

####Ubuntu
On host machine type on Terminal:
```
ifconfig | grep 'docker0' -A 1| tail -n 1| awk '/ /{print $2}'
```

It should output IP address. Like '172.17.0.1'
This 172.17.0.1 IP address is the one you need.

```
:voice-recognition-vent-socket "tcp://*:9090"
:voice-recognition-sink-socket "tcp://*:9191"
:voice-recognition-worker-vent-socket "tcp://172.17.0.1:9090"
:voice-recognition-worker-sink-socket "tcp://172.17.0.1:9191"
:core-http-url "http://172.17.0.1:3000/"
:voice-recognition-model "/tmp/model"
```
Make sure you use correct IP for worker and http url

####MacOS
```
:voice-recognition-vent-socket "tcp://*:9090"
:voice-recognition-sink-socket "tcp://*:9191"
:voice-recognition-worker-vent-socket "tcp://host.docker.internal:9090"
:voice-recognition-worker-sink-socket "tcp://host.docker.internal:9191"
:core-http-url "http://host.docker.internal:3000/"
:voice-recognition-model "/tmp/model"
```

###Docker

Run in Terminal from project root directory
```
docker pull webchange/webchange-voice-recognition-dev:latest
docker run --mount type=bind,source="$(pwd)",target=/usr/src/app webchange/webchange-voice-recognition-dev
```

Wait till you see some DEBUG messages about voice recognition.

```
2020-12-03 05:12:11,958 [main] DEBUG webchange.mq.zero-mq - Start receiving  :voice-recognition
2020-12-03 05:12:12,788 [main] DEBUG webchange.mq.zero-mq - Connection started
```
Your voice recognition should work now.
