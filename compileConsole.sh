echo $@
mvn exec:java -D exec.mainClass=ibarodf.Main -D exec.args="$@"