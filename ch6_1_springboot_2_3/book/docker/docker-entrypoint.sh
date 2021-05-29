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

function wait_dependency {
  local waited=0
  local app_name=${1}
  local app_id=${2}
  local app_port=${3}

  until curl -s http://"${app_id}"-microservice:"${app_port}"/actuator/health | grep -q "UP"; do
    echo "${app_name} microservice is unavailable - sleeping"
    sleep 1
    waited=$((waited + 1))
    if [[ ${waited} -gt 60 ]]; then
      echo "${app_name} microservice is unavailable - time out, exiting"
      exit 1
    fi
  done
  echo "${app_name} microservice is up"
}

wait_dependency "Fares" "fares" "8082"

echo "Executing command"
java -jar -Dspring.profiles.active=docker /app/book.jar
