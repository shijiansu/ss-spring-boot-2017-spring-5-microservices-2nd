#!/bin/bash

source run-all-functions.sh
validate_local_domains

/bin/bash run-all-stop.sh
/bin/bash run-config-repo-setup.sh

reset_log_files

start_rabbitmq

ha(){
  # steps to setup ha solutions
  # 1. setup different port of cluster in the bootstrap.properties with profile,
  # because it is config server, so in its resources folder;
  start_config_server_ha
  start_config_server_load_balancer

  # steps to setup ha solutions
  # 1. remove the server port in the bootstrap.properties
  # 2. setup different port of cluster in the eureka-server.yml with profile, in config-repo folder;
  # 3. setup ha profile to connect to config service load balancer endpoint, in resources folder - bootstrap-ha.properties;
  # 4. setup ha profile to connect to eureka service load balancer endpoint, in config-repo folder;
  # 5. run application with cluster profile + ha profile
  start_eureka_server_ha
  # start_eureka_server_load_balancer # not use this solution
  start_eureka_server_load_balancer_solution2

  start_fares_ha
  start_search_ha
  start_checkin_ha
  start_book_ha

  start_website_ha
}

ha # the one without memory optimized

show_ports

open_eureka_links
open_eureka_load_balancer_link

open_website_link1
