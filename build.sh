#!/bin/bash
./compile.sh
if [ -f jar/icbc-sign-message.jar ] ; then
  rm -rf jar/*.jar
fi
mkdir jar
cd ./build && jar cvfm ../jar/icbc-sign-message.jar ./META-INF/MANIFEST.MF * 
cd ../

