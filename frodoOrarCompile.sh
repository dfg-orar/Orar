mvn package
echo 'mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/orar.jar'
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/orar.jar
echo 'scp target/orar.jar kientran@frodo.informatik.uni-ulm.de:/data/kien/aaai2017Benchmark/software/orar/'
scp target/orar.jar kientran@frodo.informatik.uni-ulm.de:/data/kien/aaai2017Benchmark/software/orar/
