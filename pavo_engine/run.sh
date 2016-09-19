#!/bin/sh

cd "$( dirname "$0" )"
exec java -Xms256m -Xmx1g -cp "PAVO/build-tmp:PAVO/code/*" Engine "${PWD}/PAVO"
