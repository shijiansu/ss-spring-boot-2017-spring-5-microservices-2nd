#!/bin/bash
/bin/bash run-stop.sh
./mvnw clean spring-boot:run

# e7ecbe61-fe97-4d42-b7f6-759a71a2281f is from the springboot start up:
# 2020-08-03 23:49:38.228  INFO 52801 --- [           main] .s.s.UserDetailsServiceAutoConfiguration :
# Using generated security password: e7ecbe61-fe97-4d42-b7f6-759a71a2281f
curl http://localhost:8080/hello -u user:e7ecbe61-fe97-4d42-b7f6-759a71a2281f
