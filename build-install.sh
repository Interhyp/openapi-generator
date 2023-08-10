#! /bin/bash

TARGETDIR="/c/develop/git-hubby/api-generator"
VERSION="7.0.13-snapshot"

# build without tests (takes a little over 1 minute)
./mvnw -DskipTests -Dmaven.test.skip=true install

# remove this if you want to copy the jar
exit 0

# copy to TARGETDIR for testing in your service
cp modules/openapi-generator-cli/target/openapi-generator-cli.jar "${TARGETDIR}/openapi-generator-cli-${VERSION}.jar"
