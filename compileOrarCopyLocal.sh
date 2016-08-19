mvn package
mv target/OrarHSHOIF-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/orar.jar
echo 'orar.jar has been created in target folder'
echo 'Copying orar.jar to ~/benchmarks/software'
cp target/orar.jar ~/benchmarks/software/
