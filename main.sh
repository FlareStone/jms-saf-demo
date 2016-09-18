#!/usr/bin/env bash

cmd=$1

. bin/common_env.sh

function usage {

    echo """
Usage: $0 [p/c/u/h]
g: Generate Encrypted Passwords
c: Generating a JMS SAF Client Configuration File
z: Compact store
l: Generate WebLogic full client jar
h: Help
    """
}



case $cmd in

g)
    $JAVA_HOME/bin/java -Dweblogic.management.allowPasswordEcho=true weblogic.jms.extensions.ClientSAFEncrypt
    ;;
c)
    $JAVA_HOME/bin/java weblogic.jms.extensions.ClientSAFGenerate -moduleFile $DOMAIN_HOME/config/jms/demosystemmodule-jms.xml -outputFile $APP_HOME/config/ClientSAF_temp.xml
    ;;
z)
    $MW_HOME/oracle_common/common/bin/wlst.sh $APP_HOME/bin/compact_store.py
    ;;
l)
    cd $MW_HOME/wlserver/server/lib
    $JAVA_HOME/bin/java -jar wljarbuilder.jar
    mv $MW_HOME/wlserver/server/lib/wlfullclient.jar $APP_HOME/lib
    ;;
*)
    usage
    ;;
esac

