#! /bin/bash

TARGETDIR="/c/develop/git-hubby/api-generator"
VERSION="7.0.14-SNAPSHOT"

# build with tests (takes about 3 minutes, but should be run before releasing)
# ./mvnw clean install

# incremental build without tests (takes a little over 1 minute)
./mvnw -DskipTests -Dmaven.test.skip=true install

# remove this if you want to copy the jar
exit 0

# copy to TARGETDIR for testing in your service
cp modules/openapi-generator-cli/target/openapi-generator-cli.jar "${TARGETDIR}/openapi-generator-cli-${VERSION}.jar"

#
# to release:
#
# - fulltext search/replace 7.0.x-SNAPSHOT with 7.0.x+1-SNAPSHOT
# - build with tests, if ok, push to our branch (NO PULL REQUESTS, we are rebasing periodically)
# - https://jenkins.interhyp-intern.de/job/cloud/job/owners/job/techex/job/utilities/job/openapi-generator/job/build-image/build?delay=0sec
#

