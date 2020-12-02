#!/bin/bash

export VOICE_RECOGNITION_MODEL="/tmp/model"
#lein run init-recognizer http://alphacephei.com/vosk/models/vosk-model-small-en-us-0.4.zip
lein run init-recognizer https://alphacephei.com/vosk/models/vosk-model-en-us-daanzu-20200905-lgraph.zip

wget http://host.webchange.com/dev-gems/libvosk_jni.so
mv libvosk_jni.so /usr/src/