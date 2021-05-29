#!/bin/bash

source run-all-functions.sh
validate_local_domains

/bin/bash run-all-stop.sh
/bin/bash run-config-repo-setup.sh

reset_log_files

start_rabbitmq

ha_optimized(){
  start_config_server_ha_optimized # 2
  start_config_server_load_balancer

  start_eureka_server_ha_optimized # 2
  start_eureka_server_load_balancer_solution2

  start_fares_ha_optimized # 4 + 2
  start_search_ha_optimized # 4 + 2
  start_checkin_ha_optimized # 4 + 2
  start_book_ha_optimized # 4 + 2

  start_website_ha_optimized # 2
  start_website_load_balancer
  # total microservice applications: 30
}

ha_optimized

show_ports

open_eureka_links
open_eureka_load_balancer_link

open_website_load_balancer_link
