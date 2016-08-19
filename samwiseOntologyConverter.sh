mvn -f pomOntologyConverter.xml package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/ontologyconverter.jar
scp target/ontologyconverter.jar kientran@samwise.informatik.uni-ulm.de:/var/tmp/kien/iswc16benchmark/software/
