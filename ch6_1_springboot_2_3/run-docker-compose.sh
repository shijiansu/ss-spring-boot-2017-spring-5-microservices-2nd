#!/bin/bash
/bin/bash run-docker-compose-down.sh

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

docker-compose up -d --remove-orphans
docker-compose ps

echo ""
docker logs -f website

# open http://localhost:8001
