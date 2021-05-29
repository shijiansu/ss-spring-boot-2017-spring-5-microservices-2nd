#!/bin/bash

open http://localhost:8888/env

open http://localhost:8888/application/default
# http://localhost:8888/{service-id}/default
#{"name":"application","profiles":["default"],"label":"master", "version":"6046fd2ff4fa09d3843767660d963866ffcc7d28", "propertySources":[{"name":"file:///Users/rvlabs /config-repo /application.properties","source": {"message":"helloworld"}}]}

open http://localhost:8888/health
