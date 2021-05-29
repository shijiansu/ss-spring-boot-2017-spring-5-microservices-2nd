#!/bin/bash

start=${SECONDS}

source run-all-functions.sh
validate_local_domains

/bin/bash run-all-stop.sh
/bin/bash run-config-repo-setup.sh

reset_log_files

start_rabbitmq

start_config_server

start_eureka_server

start_fares
start_search
start_checkin
start_book
start_website

open_eureka_links
open_website_link1

echo $(( SECONDS - start ))
