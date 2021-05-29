#!/bin/bash

function _list_ports {
  # local p = xxx # fix according to https://github.com/koalaman/shellcheck/wiki/SC2155
  # -P display ports information in the result
  local p
  p=$(lsof -P \
           -i :8887 -i :8888 -i :8889 \
           -i :8760 -i :8761 -i :8762 \
           -i :8090 -i :8091 -i :8092 -i :8093 \
           -i :8095 -i :8096 \
           -i :8080 -i :8081 -i :8082 -i :8083 \
           -i :8085 -i :8086 \
           -i :8060 -i :8061 -i :8062 -i :8063 \
           -i :8065 -i :8066 \
           -i :8070 -i :8071 -i :8072 -i :8073 \
           -i :8075 -i :8076 \
           -i :8001 -i :8002)
  echo ""
  echo "Checking ports if available"
  if [[ -z "${p}" ]]; then
    echo "No running ports found... ..."
  else
    echo "Now running ports... ..."
    echo "${p}" | grep "LISTEN"
  fi
}

function _shutdown_applications {
  local p
  p=$(lsof -P \
           -i :8887 -i :8888 -i :8889 \
           -i :8760 -i :8761 -i :8762 \
           -i :8090 -i :8091 -i :8092 -i :8093 \
           -i :8095 -i :8096 \
           -i :8080 -i :8081 -i :8082 -i :8083 \
           -i :8085 -i :8086 \
           -i :8060 -i :8061 -i :8062 -i :8063 \
           -i :8065 -i :8066 \
           -i :8070 -i :8071 -i :8072 -i :8073 \
           -i :8075 -i :8076 \
           -i :8001 -i :8002)
  # tr -s ' ' - make multiple space in between to be one space only
  echo ""
  echo "Going to kill ports... ..."
  # also can kill -9 $(lsof -t), -t is only display ports
  for pid in $(echo "${p}" | grep java | tr -s ' ' | cut -d ' ' -f 2 | uniq); do
    echo "${pid}"
    kill -9 "${pid}"
  done
}

_list_ports

_shutdown_applications

_list_ports

/bin/bash run-config-repo-clean.sh

echo ""
echo "Stop and remove docker containers... ..."
docker stop config-server-lb && docker rm config-server-lb
docker stop eureka-server-lb && docker rm eureka-server-lb
docker stop website-lb && docker rm website-lb
docker stop rabbitmq && docker rm rabbitmq
