#!/bin/bash
set -eo pipefail

if [[ -n $(git status --porcelain) ]]; then
    echo Please ensure the working tree is clean
    exit 1
fi

LAST=$(git rev-parse --short HEAD)

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
git commit -m "Publish develop/$LAST"
git push origin master
git checkout develop
make clean
