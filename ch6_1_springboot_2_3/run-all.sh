#!/bin/bash

# check port to see if there are running applications
function _check_ports {
  local p
  p=$(lsof -i :8083 -i :8082 -i :8081 -i :8080 -i :8001)
  echo "Checking ports if available"
  echo "${p}"
  if [[ -n "${p}" ]]; then
    echo "[ERROR] Microservices ports are occupied, please manually kill the processor(s)"
    exit 1
  fi
}

# remove the log if exists
function _remove_log {
  local application=${1}
  [[ -f "_log/${application}.log" ]] && rm "_log/${application}.log"
}

# check rabbitmq running status, here uses the log as indicating;
# it could use rabbitmq management api also, refer to "book/docker/docker-entrypoint.sh"
function _check_rabbitmq_status {
  local container_status=${1}
  local log=${2}
  if [[ "${container_status}" != "0" ]]; then
    echo "[Docker - RabbitMQ] Failed tp start, more info at ${log}... ..."
    exit 1
  fi
  # -q: exit immediately with zero status if any match is found
  local container_name="rabbitmq"
  # check docker log for the boot up status, get the indicator of started
  while docker ps --filter "name=${container_name}" --filter status=running | grep -q "${container_name}"; do
    result=$(docker logs ${container_name} 2>&1 | grep "completed with")
    if [[ -n "${result}" ]]; then
      echo "[Docker - RabbitMQ] Started"
      break
    else
      echo "[Docker - RabbitMQ] Booting... ..."
      sleep 2s
    fi
  done
}

# check springboot application running status
function _check_spring_boot_status {
  local application=${1}
  local log="${2}"
  echo ""
  while [[ -f "${log}" ]]; do
    # check spring boot logging for the boot up status, get the indicator of started
    if grep -q "Started Application" "${log}"; then
      echo "[SpringBoot - ${application}] Started"
      break
    elif grep -q "Failed to execute goal" "${log}"; then
      echo "[SpringBoot- ${application}] Failed tp start, more info at ${log}... ..."
      exit 1
    else
      echo "[SpringBoot- ${application}] Booting... ..."
      sleep 2s
    fi
  done
}

# run springboot application
function run_spring_boot {
  local application=${1}
  local log="_log/${application}.log"
  mvn -f "${application}"/pom.xml clean spring-boot:run > "${log}" &
  echo $! >> run.pid # use this file to keep all pid so that later can stop them
  _check_spring_boot_status "${application}" "${log}"
}

# [START]
# ports are available
_check_ports

# logging
[[ ! -d _log ]] && mkdir _log
for app in "book" "checkin" "fares" "rabbitmq" "search" "website"; do
  _remove_log ${app} # clean old logs
done

# rabbitmq
docker stop rabbitmq > /dev/null 2>&1
docker rm rabbitmq > /dev/null 2>&1
docker run -d --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8.6-management > "_log/rabbitmq.log" # detached mode
_check_rabbitmq_status asdf "_log/rabbitmq.log"

# microservices
[[ -f run.pid ]] && rm run.pid

run_spring_boot "search" # 8083
run_spring_boot "fares" # 8082
run_spring_boot "book" # 8080
run_spring_boot "checkin" # 8081
run_spring_boot "website" # 8001

open http://localhost:8001
# [END]
