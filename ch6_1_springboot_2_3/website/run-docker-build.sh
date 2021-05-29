#!/bin/bash
./mvnw clean package
docker build -t shijian/website .
