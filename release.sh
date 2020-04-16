#!/bin/bash

# $1 = com.adobe.aem.demos.demo-utils dir
# $2 = version

echo "Use: ./release.sh <demo utils project dir> <version>"
echo ""
echo "Example: ./release.sh ~/Code/com.adobe.aem.demo.demo-utils 2020.04.16"

rm -rf $1/release
mkdir -p $1/release/maven
mkdir -p $1/release/github

mvn release:prepare
mvn release:perform -Darguments="-Dmaven.deploy.skip=true"

cp $1/all/target/com.adobe.aem.demo.demo-utils.all-$2.zip $1/release/github

mvn deploy:deploy-file -Durl=file://$1/release/maven -Dfile=$1/ui.content/target/com.adobe.aem.demo.demo-utils.ui.content-$2.zip -DgroupId=com.adobe.aem.demo -DartifactId=com.adobe.aem.demo.demo-utils.ui.content -Dpackaging=zip -Dversion=$2

mvn deploy:deploy-file -Durl=file://$1/release/maven -Dfile=$1/ui.apps/target/com.adobe.aem.demo.demo-utils.ui.apps-$2.zip -DgroupId=com.adobe.aem.demo -DartifactId=com.adobe.aem.demo.demo-utils.ui.apps -Dpackaging=zip -Dversion=$2
