mvn -f pomTurtleOntologyConverter.xml package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/turtleConverter.jar
scp target/turtleConverter.jar kientran@samwise.informatik.uni-ulm.de:/var/tmp/kien/iswc16benchmark/software/
