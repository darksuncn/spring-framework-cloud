#!/bin/sh

#REM *************************************************************************
#REM This script is used to start Agent application.
#REM 
#REM DEPLOY_PATH		- The deployed directory of your Agent standalone installation.
#REM JAVA_VM			- The desired Java VM to use. 
#REM JAVA_HOME		- Location of the version of Java used to start Agent application
#REM 
#REM JAVA_OPTIONS		- Java command-line options for running the server. (These
#REM					will be tagged on to the end of the JAVA_VM)
#REM					Strongly recommend that users do not change the default value
#REM CLASSPATH		- The classpath to run start procedure
#REM
#REM
#REM This scirpt also includes the following variables:
#REM 
#REM CMD_PORT			- The port of this application to execute commands, like STOP
#REM USER_VMARGS		- User specified Java VM arguments, cover JAVA_VM if it is set.
#REM EXEC_CMDLINE		- Full Java command line to start this application
#REM 
#REM *************************************************************************


echo .

DEPLOY_DIR="/home/ifp/app/springCloud-eureka-consumer"
export DEPLOY_DIR

CONF_DIR=$DEPLOY_DIR/conf

JAVA_HOME="/soft/jdk1.8.0_181"
export JAVA_HOME

JAVA_VM="-Xmx128m -Xms64m -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=2345"
export JAVA_VM


JAVA_OPTIONS=""
export JAVA_OPTIONS

echo "${DEPLOY_DIR}/lib"


LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

CLASSPATH=${LIB_JARS}
export CLASSPATH

echo "."
echo "CLASSPATH=${CLASSPATH}"
echo "."


#Set user specified JVM args
USER_VMARGS="-D64 -server  -Xmx256m -Xms128m -XX:PermSize=32m -XX:MaxPermSize=128m"
export USER_VMARGS

if [ "${USER_VMARGS}"!="" ]; then
	JAVA_VM=${USER_VMARGS}
	export JAVA_VM
fi


${JAVA_HOME}/bin/java -version

echo .

STDOUT_FILE=${DEPLOY_DIR}/logs/stdout.log
>$STDOUT_FILE

echo .

EXEC_CMDLINE="${JAVA_HOME}/bin/java ${JAVA_OPTIONS} -classpath $CONF_DIR:$LIB_JARS com.darksun.springCloud.ConsumerServiceApplication"

if [ -z "$1" ]; then

  EXEC_CMDLINE="${JAVA_HOME}/bin/java ${JAVA_VM} ${GC_OPTS} ${JAVA_DEBUG} ${JAVA_OPTIONS} -classpath $CONF_DIR:$LIB_JARS com.darksun.springCloud.ConsumerServiceApplication"
  echo "."
  echo "Execute with the following JAVA command line: ${EXEC_CMDLINE}"
  echo "."

  nohup ${EXEC_CMDLINE} > $STDOUT_FILE 2>&1 &
  echo "DONE"
else
  EXEC_CMDLINE="${EXEC_CMDLINE} $1"

  echo "."
  echo "Execute with the following JAVA command parameter line: ${EXEC_CMDLINE}"
  echo "."

  nohup ${EXEC_CMDLINE} > $STDOUT_FILE 2>&1 &
  echo "DONE"

fi

COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1 
    COUNT=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
        break
    fi
done


PIDS=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"

tail -f $STDOUT_FILE