#!/bin/bash

# make local folder as git project
cd config-repo || exit 1
git init .
git add -A .
git commit -m "Init first commit"
cd - || exit 1

# soft link from source code example
MOCK_LOCAL_GITHUB="${HOME}/30it/github"
[[ -d "${MOCK_LOCAL_GITHUB}" ]] || mkdir "${MOCK_LOCAL_GITHUB}"
[[ -d "${MOCK_LOCAL_GITHUB}/config-repo" ]] && unlink "${MOCK_LOCAL_GITHUB}/config-repo"
ln -s "$(pwd)/config-repo" "${MOCK_LOCAL_GITHUB}/config-repo"
