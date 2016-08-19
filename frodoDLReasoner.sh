mvn -f pomDLReasoner.xml package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/dlreasoner.jar
scp target/dlreasoner.jar kientran@frodo.informatik.uni-ulm.de:/data/kien/iswc16benchmark/software/
