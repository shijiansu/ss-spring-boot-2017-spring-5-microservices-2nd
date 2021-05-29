#!/bin/bash
/bin/bash run-stop.sh
./mvnw clean spring-boot:run

# run by jar
./mvnw clean package
java -jar target/greeting-service-0.0.1-SNAPSHOT.jar
curl http://localhost:8080/hello # {"message":"Hello World!"}

# HATOAS
curl http://localhost:8080/greeting?name=World!
# {"message":"Hello World!","_links":{"self":{"href":"http://localhost:8080/greeting?name=World!"}}}
## open the HAL browser - default location "http://localhost:8080/"
## key in /greeting?name=world
open http://localhost:8080/

# Swagger
## open swagger data - BUT cannot find "http://localhost:8080/swagger-ui.html"
open http://localhost:8080/swagger-resources/configuration/ui
open http://localhost:8080/v2/api-docs
