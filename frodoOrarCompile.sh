mvn package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/orar.jar
echo 'orar.jar has been created in target folder'
echo 'Copying orar.jar to Frodo Server...'
scp target/orar.jar kientran@frodo.informatik.uni-ulm.de:/data/kien/iswc16benchmark/software/
#echo 'Copying log4j.properties to Frodo Server...'
#scp target/log4j.properties kientran@frodo.informatik.uni-ulm.de:/data/kien/iswc16benchmark/software/