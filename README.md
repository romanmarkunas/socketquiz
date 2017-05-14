#Build and run
Maven build script is included.
1) cd to pom.xml folder
2) run: mvn clean install
3) find 2 jar files in folder target: server.jar and client.jar
4) run server: java -jar server.jar <number of preload lines> <path to dictionary text file>
4.1) example (if dictionary.txt file is in the same folder as jar): java -jar server.jar 50 'dictionary.txt'
5) run client: java -jar client.jar
6) to exit client/server press Ctrl+C
