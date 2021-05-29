#!/bin/bash
./mvnw clean package
docker build -t shijian/fares-microservice .
