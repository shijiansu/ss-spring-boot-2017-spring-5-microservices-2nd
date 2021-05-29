#!/bin/bash
/bin/bash run-all-stop.sh
/bin/bash run-docker-compose-down.sh
/bin/bash run-docker-compose-gateway-down.sh

docker image rm shijian/book-microservice
docker image rm shijian/checkin-microservice
docker image rm shijian/fares-microservice
docker image rm shijian/search-microservice
docker image rm shijian/website
docker image rm shijian/microservice-gateway
