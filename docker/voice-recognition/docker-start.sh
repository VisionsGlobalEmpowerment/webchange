#!/bin/bash

export config=/usr/src/app/config.edn
java -Djava.library.path=/usr/src/app/ -jar target/webchange.jar recognition-worker
