#!/bin/bash

docker logs rabbitmq

open http://localhost:15672/ # guest / guest

# example of spring cloud bus queue
open ./image/spring-cloud-bus-auto-create-queue.png
