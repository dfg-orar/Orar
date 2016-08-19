mvn -f pomdebugger.xml package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/orarDebugger.jar
scp target/orarDebugger.jar kientran@samwise.informatik.uni-ulm.de:/var/tmp/kien/iswc16benchmark/software/
