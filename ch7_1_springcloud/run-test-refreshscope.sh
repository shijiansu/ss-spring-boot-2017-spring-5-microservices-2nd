#!/bin/bash

# /refresh belongs to /actuator function, in SpringBoot2, change /actuator/refresh

# fares
http://localhost:8080/fares/hello # fares
# change fares-service.properties
curl -X POST -d {} http://localhost:8080/refresh
http://localhost:8080/fares/hello # new value

# checkin
http://localhost:8070/checkin/hello
curl -X POST -d {} http://localhost:8070/refresh
http://localhost:8070/checkin/hello

# book
http://localhost:8060/booking/hello
curl -X POST -d {} http://localhost:8060/refresh
http://localhost:8060/booking/hello

# search
http://localhost:8090/search/hello
curl -X POST -d {} http://localhost:8090/refresh
http://localhost:8090/search/hello
# change search-service.properties - "orginairports.shutdown"
curl -X POST -d {} http://localhost:8090/refresh
# search NYC would have no flight return in the UI

# spring cloud bus - will trigger "fares-service" and "search-service"
## at SpringBoot2, the endpoint is updated because actuator updated
curl -X POST http://localhost:8888/bus/refresh
