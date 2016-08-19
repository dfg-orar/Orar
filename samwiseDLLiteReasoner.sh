mvn -f pomDLLiteReasoner.xml package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/dlreasoner.jar
scp target/dlreasoner.jar kientran@samwise.informatik.uni-ulm.de:/var/tmp/kien/iswc16benchmark/software/