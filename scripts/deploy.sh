#!/bin/bash

#===========
REPOSITORY1=/home/ubuntu
REPOSITORY=/home/ubuntu/app
PROJECT_NAME=moabuja
pinpointPath=/home/ubuntu/pinpoint-agent-2.2.3-NCP-RC1

echo "> Build 파일 복사"
#cp ./build/libs/*.jar $REPOSITORY/
echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl $PROJECT_NAME | grep java | awk '{print $1}')

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

#nohup java -jar $JAR_NAME > $REPOSITORY1/nohup.out 2>&1 &
nohup java -jar \
  -javaagent:$pinpointPath/pinpoint-bootstrap-2.2.3-NCP-RC1.jar \
  -Dpinpoint.applicationName=moabuza.dev \
  -Dpinpoint.agentId=pangpang \
  -Dspring.config.location=classpath:/application.yml \
  -Dspring.profiles.active=real > $REPOSITORY1/nohup.out 2>&1 &

echo "> 마지막 $JAR_NAME 실행"

#===========

# shellcheck disable=SC2261
#nohup java -jar $JAR_NAME  1 > $REPOSITORY1/stdout.out 2 > $REPOSITORY1/stderr.out &
#nohup java -jar $JAR_NAME > /dev/null 2> /dev/null < /dev/null &
#nohup java -jar $JAR_NAME > /dev/null 2>&1 &