#!/usr/bin/bash

gradle clean
gradle starter:bootJar
docker build -t mxzero-common-web .
# docker run --rm -e SPRING_PROFILES_ACTIVE=prod  -p 8080:8080  mxzero-common-web