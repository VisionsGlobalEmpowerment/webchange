#!/bin/bash
lein clean
npx shadow-cljs release app
npx shadow-cljs release service-worker
sass ./src/cljs/webchange/ui_framework/styles/index/:./resources/public/css/
lein uberjar
java -jar ./target/webchange.jar
