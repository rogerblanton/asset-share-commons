mvn clean

mvn versions:set -DnewVersion=$1
mvn clean package -Dbnd.baseline.skip=true
git add .
git commit -m "$1 release"
git push

mvn deploy:deploy-file -Durl=file://$PWD/maven-repository/ -Dfile=$PWD/ui.content/target/com.adobe.aem.demo.demo-utils.ui.content-$1.zip -DgroupId=com.adobe.aem.demo -DartifactId=com.adobe.aem.demo.demo-utils.ui.content -Dpackaging=zip -Dversion=$1
mvn deploy:deploy-file -Durl=file://$PWD/maven-repository/ -Dfile=$PWD/ui.apps/target/com.adobe.aem.demo.demo-utils.ui.apps-$1.zip -DgroupId=com.adobe.aem.demo -DartifactId=com.adobe.aem.demo.demo-utils.ui.apps -Dpackaging=zip -Dversion=$1

mvn versions:set -DnewVersion=$2
git add .
git commit -m "$3 release"
git push
