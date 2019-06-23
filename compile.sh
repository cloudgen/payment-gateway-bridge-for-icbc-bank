#!/bin/bash
rm build/org/kpex/*.class
javac -cp build -d build -sourcepath src src/org/kpex/ICBCSignMsg.java
