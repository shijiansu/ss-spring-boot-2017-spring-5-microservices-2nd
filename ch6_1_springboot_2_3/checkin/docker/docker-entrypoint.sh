#!/bin/bash
set -e

# -i show header so that grep status code
waited=0
until curl -si -u guest:guest http://message-queue:15672/api/overview | grep -q "200 OK"; do
  echo "RabbitMQ is unavailable - sleeping"
  sleep 1
  waited=$((waited + 1))
  if [[ ${waited} -gt 60 ]]; then # set 60s as timeout, not longer waiting
    echo "RabbitMQ is unavailable - time out, exiting"
    exit 1
  fi
done
echo "RabbitMQ is up"

echo "Executing command"
java -jar -Dspring.profiles.active=docker /app/checkin.jar

