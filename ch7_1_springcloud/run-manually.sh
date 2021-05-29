#!/bin/bash

# rabbitmq
## docker stop rabbitmq && docker rm rabbitmq
## docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8.6-management # interaction mode
docker run -d --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8.6-management # detached mode
docker logs -f rabbitmq
docker stop rabbitmq # will rm rabbitmq because --rm

# configuration server
mvn -f config-server/pom.xml clean spring-boot:run # 8888

# eureka server
## setup the local dns
sudo vi /etc/hosts
127.0.0.1 eureka-server-1
127.0.0.1 eureka-server-2

mvn -f eureka-server/pom.xml clean spring-boot:run -Drun.profiles=1 # 8761
mvn -f eureka-server/pom.xml clean spring-boot:run -Drun.profiles=2 # 8762

open http://eureka-server-1:8761
open http://eureka-server-2:8762

# microservices
mvn -f sc-search/pom.xml clean spring-boot:run # 8090
mvn -f sc-fares/pom.xml clean spring-boot:run # 8080
mvn -f sc-book/pom.xml clean spring-boot:run # 8060
mvn -f sc-checkin/pom.xml clean spring-boot:run # 8070

mvn -f sc-search-apigateway/pom.xml clean spring-boot:run # 8095
mvn -f sc-fares-apigateway/pom.xml clean spring-boot:run # 8085
mvn -f sc-book-apigateway/pom.xml clean spring-boot:run # 8065
mvn -f sc-checkin-apigateway/pom.xml clean spring-boot:run # 8075

mvn -f sc-website/pom.xml clean spring-boot:run # 8001

# testing
## eureka server
open http://localhost:8761
## website
open http://localhost:8001
## refresh scope - for value change in "search-service.properties" - "orginairports.shutdown"
curl -X POST -d {} http://localhost:8090/refresh

# check with actuator - fares service
curl http://localhost:8080/info
