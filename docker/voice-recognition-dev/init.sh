#!/bin/bash

export VOICE_RECOGNITION_MODELS_DIR="/tmp/model"

java -Djava.library.path=/usr/src/app/ -jar target/webchange.jar add-model english https://alphacephei.com/vosk/models/vosk-model-en-us-daanzu-20200905-lgraph.zip

java -Djava.library.path=/usr/src/app/ -jar target/webchange.jar add-model spanish https://alphacephei.com/vosk/models/vosk-model-small-es-0.3.zip

wget http://host.webchange.com/dev-gems/libvosk_jni.so
mv libvosk_jni.so /usr/src/
