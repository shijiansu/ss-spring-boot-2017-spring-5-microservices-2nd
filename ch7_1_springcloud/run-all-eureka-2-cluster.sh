#!/bin/bash

source run-all-functions.sh
validate_local_domains

/bin/bash run-all-stop.sh
/bin/bash run-config-repo-setup.sh

reset_log_files

start_rabbitmq

start_config_server

start_eureka_server

open_eureka_links
