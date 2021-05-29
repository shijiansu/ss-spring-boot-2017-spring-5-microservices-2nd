#!/bin/bash
./mvnw clean package
docker build -t shijian/search-microservice .
