#!/bin/bash

export DOCKER_HOST_IP=$(route -n | awk '/UG[ \t]/{print $2}')
export VOICE_RECOGNITION_VENT_SOCKET="tcp://"$DOCKER_HOST_IP":9090"
export VOICE_RECOGNITION_SINK_SOCKET="tcp://"$DOCKER_HOST_IP":9191"
export CORE_HTTP_URL="http://"$DOCKER_HOST_IP":3000/"
export VOICE_RECOGNITION_MODEL="/tmp/model"
cp /usr/src/libvosk_jni.so /usr/src/app/native/vosk/

lein run recognition-worker
