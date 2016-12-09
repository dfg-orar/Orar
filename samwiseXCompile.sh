mvn -f pomX.xml package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/X.jar
echo 'X.jar has been created in target folder'
echo 'Copying X.jar to Samwise Server...'
scp target/X.jar kientran@samwise.informatik.uni-ulm.de:/media/data/kien/aaai2017benchmark/software/xsystem/
