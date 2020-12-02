#!/bin/bash

export VOICE_RECOGNITION_MODEL="/tmp/model"
cp /usr/src/libvosk_jni.so /usr/src/app/native/vosk/

lein run recognition-worker
