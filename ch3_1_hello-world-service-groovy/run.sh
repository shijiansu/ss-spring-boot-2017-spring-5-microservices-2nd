#!/bin/bash

sdk list springboot # install "spring-cli" with "sdkman"
sdk install springboot 2.3.2.RELEASE

spring --version # verify installed springboot cli

spring run first-app.groovy # run springboot

curl http://localhost:8080 # Hello World!
