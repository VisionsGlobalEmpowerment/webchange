#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd)"
cd $DIR
cd ../
mkdir distr
cp $DIR/school-setup/* distr
cp dump-secondary.sql distr
lein uberjar
cp target/webchange.jar distr/current.jar
zip -r distr/raw.zip resources/public/raw