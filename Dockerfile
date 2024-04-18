FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DshipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/pbl5-server-0.0.1-SNAPSHOT.jar pbl5-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pbl5-server.jar"]
