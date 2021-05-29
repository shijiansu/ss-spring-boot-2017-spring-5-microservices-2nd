#!/bin/bash

# [START] Optimize JVM memory allocation
# 1. Maven (optional, because change spring-boot:run to java -jar, if use spring-boot:run, some memory occupies by Maven)
# 2. Java runtime
# 3. Replace spring-boot:run, so that save memory of Maven allocated
# Maven - this allocates per execution of "spring-boot:run"
export MAVEN_OPTS="-Xmx50M"

# JVM parameters - this allocates per application
## SpringBoot 1:
## -Drun.jvmArguments - https://docs.spring.io/spring-boot/docs/1.5.2.RELEASE/maven-plugin/run-mojo.html
## SpringBoot 2:
## -Dspring-boot.run.jvmArguments - https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/maven-plugin/run-mojo.html
RUN_OPTS='-Drun.jvmArguments="-Xmx50M"'
JAR_OPTS="-Xmx50M"
# [END]

validate_local_domains(){
  ping -c 1 eureka-server-1
  local r1=$?
  ping -c 1 eureka-server-2
  local r2=$?
  ping -c 1 eureka-server-load-balancer
  local r3=$?
  ping -c 1 config-server-load-balancer
  local r4=$?
  ping -c 1 website-load-balancer
  local r5=$?
  if [[ $r1 -ne 0 || $r2 -ne 0 || $r3 -ne 0 || $r4 -ne 0 || $r5 -ne 0 ]]; then
    echo "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    echo "Please setup local dns for below domains... ..."
    echo ""
    echo "sudo vi /etc/hosts"
    echo "127.0.0.1 eureka-server-1"
    echo "127.0.0.1 eureka-server-2"
    echo "127.0.0.1 eureka-server-load-balancer"
    echo "127.0.0.1 config-server-load-balancer"
    echo "127.0.0.1 website-load-balancer"
    echo "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
  exit 1
  fi
}

reset_log_files(){
  for file in $(ls _log/*.log 2> /dev/null || true); do [[ -f "${file}" ]] && rm "${file}"; done
  [[ ! -d _log ]] && mkdir _log
}

# rabbitmq
start_rabbitmq(){
  docker run -d --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8.6-management # detached mode
  sleep 20
}

# config server
start_config_server(){
  mvn -f config-server/pom.xml clean spring-boot:run > "_log/config-server.log" & # 8888
  sleep 20
}

# config server ha
start_config_server_ha(){
  mvn -f config-server/pom.xml clean spring-boot:run -Drun.profiles=1 "${RUN_OPTS}" > "_log/config-server-1-ha.log" & # 8888
  mvn -f config-server/pom.xml clean spring-boot:run -Drun.profiles=2 "${RUN_OPTS}" > "_log/config-server-2-ha.log" & # 8889
  sleep 20
}

# config server ha - only build once
start_config_server_ha_optimized(){
  # seperate spring-boot:run to 2 steps - build and run
  mvn -f config-server/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1 config-server/target/config-server.jar > "_log/config-server-1-ha.log" & # 8888
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2 config-server/target/config-server.jar > "_log/config-server-2-ha.log" & # 8889
  sleep 20
}

# config server load balancer -> load balancing to port 8888 and 8889
start_config_server_load_balancer(){
  # ip solution comes out at the eureka first.
  # design details refer to "start_eureka_server_load_balancer_solution2"
  cd config-server-lb || exit 1
  /bin/bash run-docker-build.sh
  cd - || exit 1

  local host_ip
  host_ip=$(ipconfig getifaddr en0)
  ping -c 1 "${host_ip}"
  local r=$?
  if [[ $r -ne 0 ]]; then
    echo "Failed to get the host ip... ..."
    exit 1
  fi
  docker run -d --rm --name config-server-lb -p 8887:8887 \
    --add-host config-server-1:"${host_ip}" \
    --add-host config-server-2:"${host_ip}" \
    shijian/config-server-lb # detached mode
  sleep 20
}

# eureka server - standalone
start_eureka_server_standalone(){
  mvn -f eureka-server/pom.xml clean spring-boot:run > "_log/eureka-server.log" & # 8761
  sleep 25
}

# eureka server - cluster
start_eureka_server(){
  mvn -f eureka-server/pom.xml clean spring-boot:run -Drun.profiles=1 > "_log/eureka-server-1.log" & # 8761
  sleep 25
  mvn -f eureka-server/pom.xml clean spring-boot:run -Drun.profiles=2 > "_log/eureka-server-2.log" & # 8762
  sleep 25
}

# eureka server - cluster - connect to ha config server solution
# use spring profile to enable the ha
start_eureka_server_ha(){
  mvn -f eureka-server/pom.xml clean spring-boot:run -Drun.profiles=1,ha "${RUN_OPTS}" > "_log/eureka-server-1-ha.log" & # 8761
  sleep 25
  mvn -f eureka-server/pom.xml clean spring-boot:run -Drun.profiles=2,ha "${RUN_OPTS}" > "_log/eureka-server-2-ha.log" & # 8762
  sleep 25
}

# eureka server - clsuter - connect to ha config server solution
start_eureka_server_ha_optimized(){
  mvn -f eureka-server/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha eureka-server/target/eureka-server.jar > "_log/eureka-server-1-ha.log" & # 8761
  sleep 25
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha eureka-server/target/eureka-server.jar > "_log/eureka-server-2-ha.log" & # 8762
  sleep 25
}

# eureka server load balancer -> load balancing to port 8761 and 8762
start_eureka_server_load_balancer(){
  cd eureka-server-lb || exit 1
  /bin/bash run-docker-build.sh
  cd - || exit 1

  # docker logs eureka-server-lb
  # docker inspect eureka-server-lb | grep IPAddress
  # docker run -it --name eureka-server-lb --net=host shijian/eureka-server-lb /bin/bash
  # docker stop eureka-server-lb && docker rm eureka-server-lb
  docker run -d --rm --name eureka-server-lb -p 8760:8760 shijian/eureka-server-lb # detached mode
  sleep 20
}

# eureka server load balancer -> load balancing to port 8761 and 8762
start_eureka_server_load_balancer_solution2(){
  cd eureka-server-lb-solution2 || exit 1
  /bin/bash run-docker-build.sh
  cd - || exit 1

  # https://apple.stackexchange.com/questions/20547/how-do-i-find-my-ip-address-from-the-command-line
  local host_ip
  host_ip=$(ipconfig getifaddr en0) # could be not working, so add some validation in next line
  ping -c 1 "${host_ip}"
  local r=$?
  if [[ $r -ne 0 ]]; then
    echo "Failed to get the host ip... ..."
    exit 1
  fi
  # both eureka-server-1 and eureka-server-2 in the host /etc/hosts map to 127.0.0.1
  docker run -d --rm --name eureka-server-lb -p 8760:8760 \
    --add-host eureka-server-1:"${host_ip}" \
    --add-host eureka-server-2:"${host_ip}" \
    shijian/eureka-server-lb # detached mode
  sleep 20
}

# fares microservices
start_fares(){
  mvn -f sc-fares/pom.xml clean spring-boot:run > "_log/fares.log" & # 8080
  sleep 35
  mvn -f sc-fares-apigateway/pom.xml clean spring-boot:run > "_log/fares-apigateway.log" & # 8085
  sleep 25
}

# fares microservices ha
start_fares_ha(){
  mvn -f sc-fares/pom.xml clean spring-boot:run -Drun.profiles=1,ha "${RUN_OPTS}" > "_log/fares-1-ha.log" & # 8080
  sleep 35
  mvn -f sc-fares/pom.xml clean spring-boot:run -Drun.profiles=2,ha "${RUN_OPTS}" > "_log/fares-2-ha.log" & # 8081
  sleep 35
  mvn -f sc-fares/pom.xml clean spring-boot:run -Drun.profiles=3,ha "${RUN_OPTS}" > "_log/fares-3-ha.log" & # 8082
  sleep 35
  mvn -f sc-fares/pom.xml clean spring-boot:run -Drun.profiles=4,ha "${RUN_OPTS}" > "_log/fares-4-ha.log" & # 8083
  sleep 35
  mvn -f sc-fares-apigateway/pom.xml clean spring-boot:run -Drun.profiles=1,ha "${RUN_OPTS}" > "_log/fares-apigateway-1-ha.log" & # 8085
  sleep 25
  mvn -f sc-fares-apigateway/pom.xml clean spring-boot:run -Drun.profiles=2,ha "${RUN_OPTS}" > "_log/fares-apigateway-2-ha.log" & # 8086
  sleep 25
}

# fares microservices ha
start_fares_ha_optimized(){
  mvn -f sc-fares/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-fares/target/sc-fares.jar > "_log/fares-1-ha.log" & # 8080
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-fares/target/sc-fares.jar > "_log/fares-2-ha.log" & # 8081
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=3,ha sc-fares/target/sc-fares.jar > "_log/fares-3-ha.log" & # 8082
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=4,ha sc-fares/target/sc-fares.jar > "_log/fares-4-ha.log" & # 8083
  sleep 35
  mvn -f sc-fares-apigateway/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-fares-apigateway/target/sc-fares-apigateway.jar > "_log/fares-apigateway-1-ha.log" & # 8085
  sleep 25
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-fares-apigateway/target/sc-fares-apigateway.jar > "_log/fares-apigateway-2-ha.log" & # 8086
  sleep 25
}

# search microservices
start_search(){
  mvn -f sc-search/pom.xml clean spring-boot:run > "_log/search.log" & # 8090
  sleep 35
  mvn -f sc-search-apigateway/pom.xml clean spring-boot:run > "_log/search-apigateway.log" & # 8095
  sleep 25
}

# search microservices ha
start_search_ha(){
  mvn -f sc-search/pom.xml clean spring-boot:run -Drun.profiles=1,ha > "_log/search-1-ha.log" & # 8090
  sleep 35
  mvn -f sc-search/pom.xml clean spring-boot:run -Drun.profiles=2,ha > "_log/search-2-ha.log" & # 8091
  sleep 35
  mvn -f sc-search/pom.xml clean spring-boot:run -Drun.profiles=3,ha > "_log/search-3-ha.log" & # 8092
  sleep 35
  mvn -f sc-search/pom.xml clean spring-boot:run -Drun.profiles=4,ha > "_log/search-4-ha.log" & # 8093
  sleep 35
  mvn -f sc-search-apigateway/pom.xml clean spring-boot:run -Drun.profiles=1,ha > "_log/search-apigateway-1-ha.log" & # 8095
  sleep 25
  mvn -f sc-search-apigateway/pom.xml clean spring-boot:run -Drun.profiles=2,ha > "_log/search-apigateway-2-ha.log" & # 8096
  sleep 25
}

# search microservices ha
start_search_ha_optimized(){
  mvn -f sc-search/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-search/target/sc-search.jar > "_log/search-1-ha.log" & # 8090
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-search/target/sc-search.jar > "_log/search-2-ha.log" & # 8091
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=3,ha sc-search/target/sc-search.jar > "_log/search-3-ha.log" & # 8092
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=4,ha sc-search/target/sc-search.jar > "_log/search-4-ha.log" & # 8093
  sleep 35
  mvn -f sc-search-apigateway/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-search-apigateway/target/sc-search-apigateway.jar > "_log/search-apigateway-1-ha.log" & # 8095
  sleep 25
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-search-apigateway/target/sc-search-apigateway.jar > "_log/search-apigateway-2-ha.log" & # 8096
  sleep 25
}

# checkin microservices
start_checkin(){
  mvn -f sc-checkin/pom.xml clean spring-boot:run > "_log/checkin.log" & # 8070
  sleep 35
  mvn -f sc-checkin-apigateway/pom.xml clean spring-boot:run > "_log/checkin-apigateway.log" & # 8075
  sleep 25
}

# checkin microservices ha
start_checkin_ha(){
  mvn -f sc-checkin/pom.xml clean spring-boot:run -Drun.profiles=1,ha > "_log/checkin-1-ha.log" & # 8070
  sleep 35
  mvn -f sc-checkin/pom.xml clean spring-boot:run -Drun.profiles=2,ha > "_log/checkin-2-ha.log" & # 8071
  sleep 35
  mvn -f sc-checkin/pom.xml clean spring-boot:run -Drun.profiles=3,ha > "_log/checkin-3-ha.log" & # 8072
  sleep 35
  mvn -f sc-checkin/pom.xml clean spring-boot:run -Drun.profiles=4,ha > "_log/checkin-4-ha.log" & # 8073
  sleep 35
  mvn -f sc-checkin-apigateway/pom.xml clean spring-boot:run -Drun.profiles=1,ha > "_log/checkin-apigateway-1-ha.log" & # 8075
  sleep 25
  mvn -f sc-checkin-apigateway/pom.xml clean spring-boot:run -Drun.profiles=2,ha > "_log/checkin-apigateway-2-ha.log" & # 8076
  sleep 25
}

# checkin microservices ha
start_checkin_ha_optimized(){
  mvn -f sc-checkin/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-checkin/target/sc-checkin.jar > "_log/checkin-1-ha.log" & # 8070
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-checkin/target/sc-checkin.jar > "_log/checkin-2-ha.log" & # 8071
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=3,ha sc-checkin/target/sc-checkin.jar > "_log/checkin-3-ha.log" & # 8072
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=4,ha sc-checkin/target/sc-checkin.jar > "_log/checkin-4-ha.log" & # 8073
  sleep 35
  mvn -f sc-checkin-apigateway/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-checkin-apigateway/target/sc-checkin-apigateway.jar > "_log/checkin-apigateway-1-ha.log" & # 8075
  sleep 25
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-checkin-apigateway/target/sc-checkin-apigateway.jar > "_log/checkin-apigateway-2-ha.log" & # 8076
  sleep 25
}

# book microservices
start_book(){
  mvn -f sc-book/pom.xml clean spring-boot:run > "_log/book.log" & # 8060
  sleep 35
  mvn -f sc-book-apigateway/pom.xml clean spring-boot:run > "_log/book-apigateway.log" & # 8065
  sleep 25
}

# book microservices ha
start_book_ha(){
  mvn -f sc-book/pom.xml clean spring-boot:run -Drun.profiles=1,ha > "_log/book-1-ha.log" & # 8060
  sleep 35
  mvn -f sc-book/pom.xml clean spring-boot:run -Drun.profiles=2,ha > "_log/book-2-ha.log" & # 8061
  sleep 35
  mvn -f sc-book/pom.xml clean spring-boot:run -Drun.profiles=3,ha > "_log/book-3-ha.log" & # 8062
  sleep 35
  mvn -f sc-book/pom.xml clean spring-boot:run -Drun.profiles=4,ha > "_log/book-4-ha.log" & # 8063
  sleep 35
  mvn -f sc-book-apigateway/pom.xml clean spring-boot:run -Drun.profiles=1,ha > "_log/book-apigateway-1-ha.log" & # 8065
  sleep 25
  mvn -f sc-book-apigateway/pom.xml clean spring-boot:run -Drun.profiles=2,ha > "_log/book-apigateway-2-ha.log" & # 8066
  sleep 25
}

# book microservices ha
start_book_ha_optimized(){
  mvn -f sc-book/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-book/target/sc-book.jar > "_log/book-1-ha.log" & # 8060
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-book/target/sc-book.jar > "_log/book-2-ha.log" & # 8061
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=3,ha sc-book/target/sc-book.jar > "_log/book-3-ha.log" & # 8062
  sleep 35
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=4,ha sc-book/target/sc-book.jar > "_log/book-4-ha.log" & # 8063
  sleep 35
  mvn -f sc-book-apigateway/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-book-apigateway/target/sc-book-apigateway.jar > "_log/book-apigateway-1-ha.log" & # 8065
  sleep 25
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-book-apigateway/target/sc-book-apigateway.jar > "_log/book-apigateway-2-ha.log" & # 8066
  sleep 25
}

# website
start_website(){
  mvn -f sc-website/pom.xml clean spring-boot:run > "_log/website.log" & # 8001
  sleep 60
}

# website ha
start_website_ha(){
  mvn -f sc-website/pom.xml clean spring-boot:run -Drun.profiles=1,ha > "_log/website-1-ha.log" & # 8001
  sleep 60
  mvn -f sc-website/pom.xml clean spring-boot:run -Drun.profiles=2,ha > "_log/website-2-ha.log" & # 8002
  sleep 60
}

# website ha
start_website_ha_optimized(){
  mvn -f sc-website/pom.xml clean package
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=1,ha sc-website/target/sc-website.jar > "_log/website-1-ha.log" & # 8001
  sleep 60
  java "${JAR_OPTS}" -jar -Dspring.profiles.active=2,ha sc-website/target/sc-website.jar > "_log/website-2-ha.log" & # 8002
  sleep 60
}

# website load balancer -> load balancing to port 8001 and 8002
start_website_load_balancer(){
  cd sc-website-lb || exit 1
  /bin/bash run-docker-build.sh
  cd - || exit 1

  local host_ip
  host_ip=$(ipconfig getifaddr en0)
  ping -c 1 "${host_ip}"
  local r=$?
  if [[ $r -ne 0 ]]; then
    echo "Failed to get the host ip... ..."
    exit 1
  fi
  docker run -d --rm --name sc-website-lb -p 8000:8000 \
    --add-host website-1:"${host_ip}" \
    --add-host website-2:"${host_ip}" \
    shijian/sc-website-lb # detached mode
  sleep 20
}

# open eureka server link
open_eureka_link1(){
  open http://localhost:8761
}

# open eureka server link
open_eureka_link2(){
  open http://localhost:8762
}

# open eureka server link
open_eureka_links(){
  open_eureka_link1
  open_eureka_link2
}

# open eureka servers load balancer link
open_eureka_load_balancer_link(){
  open http://localhost:8760
}

# open website link
open_website_link1(){
  open http://localhost:8001
}

# open website link
open_website_link2(){
  open http://localhost:8002
}

# open website load balancer link
open_website_load_balancer_link(){
  open http://localhost:8000
}

show_ports(){
  echo ""
  echo "Checking ports if available"
  lsof -P \
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
       -i :8001 -i :8002 | grep java | grep LISTEN
}
