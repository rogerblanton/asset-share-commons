mvn clean

mvn versions:set -DnewVersion=$1
mvn clean package -U -Dbnd.baseline.skip=true
git add .
git commit -m "$1 release"
git push