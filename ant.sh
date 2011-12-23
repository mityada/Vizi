#!/bin/sh

JAVA_HOME=/usr/lib/jvm/java-6-openjdk
export JAVA_HOME

CLASSPATH=`find meta/bin/lib -maxdepth 3 -name '*.jar' -printf '%p:'`$CLASSPATH
CLASSPATH=`find $JAVA_HOME -maxdepth 4 -name '*.jar' -printf '%p:'`$CLASSPATH
export CLASSPATH

exec \
java -classpath "$CLASSPATH" \
org.apache.tools.ant.Main $*
