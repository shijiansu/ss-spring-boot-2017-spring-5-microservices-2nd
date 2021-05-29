#!/bin/bash

# rabbitmq
# docker stop rabbitmq && docker rm rabbitmq
# docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8.6-management # interaction mode
docker run -d --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8.6-management # detached mode
docker logs -f rabbitmq
docker stop rabbitmq # will rm rabbitmq because --rm

# microservices - with logging
mvn -f search/pom.xml clean spring-boot:run # 8083
mvn -f fares/pom.xml clean spring-boot:run # 8082
mvn -f book/pom.xml clean spring-boot:run # 8080
mvn -f checkin/pom.xml clean spring-boot:run # 8081
mvn -f website/pom.xml clean spring-boot:run # 8001

open http://localhost:8001
