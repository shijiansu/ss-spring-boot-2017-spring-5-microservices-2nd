#!/bin/bash
# Run SMTP server - the dependency of customer-notification microservice
## download - FakeSMTP http://nilhcem.com/FakeSMTP/
## git clone https://github.com/Nilhcem/FakeSMTP.git
# java -jar fakeSMTP-VERSION.jar
docker build -f FakeSMTP/Dockerfile FakeSMTP -t fakesmtp:2.0
docker stop fakesmtp && docker rm fakesmtp
docker run -it --rm --name fakesmtp -p 250:25 fakesmtp:2.0

# Run RabbitMQ server - the dependency of customer and customer-notification microservice
## https://www.rabbitmq.com/download.html
## ./rabbitmq-server
docker stop rabbitmq && docker rm rabbitmq
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.8.6-management

# to detect of port occupation, if apply
lsof -nP -iTCP:8080 | grep LISTEN
kill -9 ${PID}
## kill -9 $(lsof -nP -iTCP:8080 | grep LISTEN | cut -d' ' -f5)

# start customer microservice
./mvnw -f customer/pom.xml spring-boot:run

# start customer-notification microservice
./mvnw -f customer-notification/pom.xml spring-boot:run

# testing data
curl -H 'Accept: application/json' -H 'Content-type: application/json' \
  -X POST -d '{"name": "tester", "email": "tester@google.com"}' \
  localhost:8080/register

# log
## curl
{"id":8,"name":"tester","email":"tester@google.com"}
## customer service
Ready to send message but suppressed tester@google.com
## rabbitmq - no log
## customer-notification service
tester@google.com
## fake smtp
11 Aug 2020 15:55:01 DEBUG org.subethamail.smtp.server.Session - Client: MAIL FROM:<shijiansu@sjh>
11 Aug 2020 15:55:01 DEBUG org.subethamail.smtp.server.Session - Server: 250 Ok
11 Aug 2020 15:55:01 DEBUG org.subethamail.smtp.server.Session - Client: RCPT TO:<tester@google.com>
11 Aug 2020 15:55:01 DEBUG org.subethamail.smtp.server.Session - Server: 250 Ok

# clean up
ctrl + c
