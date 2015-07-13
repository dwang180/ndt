#!/bin/bash
java/mac/jre/bin/java -Dlog4j.configurationFile=conf/log4j2.xml  -jar networkdiagnostic-1.0.jar $*
