mvn -f pomOntologyConverter.xml package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/ontologyconverter2OWL.jar
scp target/ontologyconverter2OWL.jar kientran@samwise.informatik.uni-ulm.de:/media/data/kien/aaai2017benchmark/software/converter/
