# Run database with docker-compose
cd ../
docker-compose up -d

# Run server by mvn command (required maven 3.6.1 version and java 17)
mvn clean
mvn package -DskipTests
mvn spring-boot:run -Dspring-boot.run.profiles=dev