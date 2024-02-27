# JAR="../build/libs/mytaek1-0.0.1-SNAPSHOT.jar"
# STOP_LOG="/var/log/aws/codedeploy-agent/stop.log"
# SERVICE_PID=$(pgrep -f $JAR) # 실행중인 Spring 서버의 PID

# if [ -z "$SERVICE_PID" ]; then
#   echo "서비스 NouFound" >> $STOP_LOG
# else
#   echo "서비스 종료 " >> $STOP_LOG
#   kill "$SERVICE_PID"
# fi


#!/bin/bash

ROOT_PATH="/home/ubuntu/spring-github-action"
JAR="$ROOT_PATH/application.jar"
STOP_LOG="/var/log/aws/codedeploy-agent/stop.log"
SERVICE_PID=$(pgrep -f $JAR) # 실행중인 Spring 서버의 PID

if [ -z "$SERVICE_PID" ]; then
  echo "서비스 NotFound" >> $STOP_LOG
else
  echo "서비스 종료 " >> $STOP_LOG
  kill -9 "$SERVICE_PID"
fi

