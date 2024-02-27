# JAR="$ROOT_PATH/application.jar"
JAR="../build/libs/mytaek1-0.0.1-SNAPSHOT.jar"
STOP_LOG="/log/stop.log"
SERVICE_PID=$(pgrep -f $JAR) # 실행중인 Spring 서버의 PID

if [ -z "$SERVICE_PID" ]; then
  echo "서비스 NouFound" >> $STOP_LOG
else
  echo "서비스 종료 " >> $STOP_LOG
  kill "$SERVICE_PID"
fi
