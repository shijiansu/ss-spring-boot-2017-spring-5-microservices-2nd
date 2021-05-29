#!/bin/bash
/bin/bash run-docker-compose-gateway-down.sh

function docker_build {
  local application=${1}
  cd "${application}" || exit 1
  /bin/bash run-docker-build.sh
  cd ..
}

docker_build "book"
docker_build "checkin"
docker_build "fares"
docker_build "search"
docker_build "website"
docker_build "microservice-gateway"

docker-compose -f docker-compose-gateway.yml up -d --remove-orphans
docker-compose ps

echo ""
# docker logs -f website
# docker logs -f book-microservice
docker logs -f microservice-gateway

# open http://localhost:8001
