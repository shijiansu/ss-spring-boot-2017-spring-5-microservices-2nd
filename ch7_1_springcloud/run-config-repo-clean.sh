#!/bin/bash

# make local folder as git project
cd config-repo || exit 1
rm -rf .git
cd - || exit 1

MOCK_LOCAL_GITHUB="${HOME}/30it/github"
[[ -d "${MOCK_LOCAL_GITHUB}/config-repo" ]] && unlink "${MOCK_LOCAL_GITHUB}/config-repo"
