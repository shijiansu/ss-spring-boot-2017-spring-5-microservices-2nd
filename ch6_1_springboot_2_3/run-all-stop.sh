#!/bin/bash

# microservices
if [[ -f run.pid ]]; then
  # kill spring boot application processors
  while IFS= read -r pid; do
    # only kill it when it exists
    if [[ $(ps -p "${pid}" | wc -l) -gt 1 ]]; then
      echo "kill -9 ${pid}"
      kill -9 "${pid}"
    fi
  done < "run.pid"
fi

# rabbitmq
docker stop rabbitmq > /dev/null 2>&1
docker rm rabbitmq > /dev/null 2>&1

# clean up
[[ -f run.pid ]] && rm run.pid
