# Run database with docker-compose
cd ../
docker-compose up -d
sleep 2
PGPASSWORD=TwwlZL9j10wyziG3 psql -h localhost -p 6002 -U qh47Qsmu19JJRuMq -d job_swipe -f db/initial_db.sql

# Run server by mvn command (required maven 3.6.1 version and java 17)
mvn clean
mvn package -DskipTests
mvn spring-boot:run -Dspring-boot.run.profiles=dev