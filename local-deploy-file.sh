#!/bin/bash

# $1 = output dir
# $2 = com.adobe.aem.demos.demo-utils dir
# $3 = version

echo "Use: ./local-deploy-file.sh <output dir> <demo utils project dir> <version>"
echo ""
echo "Example: ./local-deploy-file.sh ~/local-maven-deploy ~/Code/com.adobe.aem.demo.demo-utils 2020.04.16"

mvn deploy:deploy-file -Durl=file://$1 -Dfile=$2/ui.content/target/com.adobe.aem.demo.demo-utils.ui.content-$3.zip -DgroupId=com.adobe.aem.demo -DartifactId=com.adobe.aem.demo.demo-utils.ui.content -Dpackaging=zip -Dversion=$3
mvn deploy:deploy-file -Durl=file://$1 -Dfile=$2/ui.apps/target/com.adobe.aem.demo.demo-utils.ui.apps-$3.zip -DgroupId=com.adobe.aem.demo -DartifactId=com.adobe.aem.demo.demo-utils.ui.apps -Dpackaging=zip -Dversion=$3
