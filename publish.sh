#!/bin/bash
set -eo pipefail

git push origin develop

make clean
make site

git checkout master

rsync -a                       \
      --filter='P _site/'      \
      --filter='P _cache/'     \
      --filter='P .git/'       \
      --filter='P .gitignore'  \
      --filter='P .stack-work' \
      --delete-excluded        \
      _site/ .

git add .

git commit -m 'Publish'

git push origin master

git checkout develop
