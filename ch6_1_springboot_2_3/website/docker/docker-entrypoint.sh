#!/bin/bash
set -e

function wait_dependency {
  local waited=0
  local app_name=${1}
  local app_id=${2}
  local app_port=${3}

  until curl -s http://"${app_id}":"${app_port}"/actuator/health | grep -q "UP"; do
    echo "${app_name} microservice is unavailable - sleeping"
    sleep 1
    waited=$((waited + 1))
    if [[ ${waited} -gt 120 ]]; then # set 60s as timeout, not longer waiting
      echo "${app_name} microservice is unavailable - time out, exiting"
      exit 1
    fi
  done
  echo "${app_name} microservice is up"
}

# ${book-microservice} is from the environment variable,
# set the default to the hostname / container name if there is no gateway in the architecture.
# if the microservice-gateway exists, then website would not directly call to individual microservice,
# it hits the microservice gateway always.
wait_dependency "Search" "${ENV_GATEWAY_HOSTNAME:-search-microservice}" "8083"
wait_dependency "Checkin" "${ENV_GATEWAY_HOSTNAME:-checkin-microservice}" "8081"
wait_dependency "Book" "${ENV_GATEWAY_HOSTNAME:-book-microservice}" "8080"

echo "Executing command"
java -jar -Dspring.profiles.active=docker /app/website.jar
