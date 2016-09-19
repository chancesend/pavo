#!/bin/sh

cd "$( dirname "$0" )"
rm -rf PAVO/build-tmp
mkdir PAVO/build-tmp
javac -cp "PAVO/code/*" -d PAVO/build-tmp PAVO/*.java
