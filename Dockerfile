FROM maven:3.8.5-openjdk-17 AS build
COPY . .
USER root
COPY scripts/credentials /root/.aws/credentials
RUN mvn clean
RUN mvn package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /root/.aws/credentials /root/.aws/credentials
COPY --from=build /target/job-swipe-0.0.1-SNAPSHOT.jar job-swipe.jar
EXPOSE 8080 8888
ENTRYPOINT ["java", "-jar", "job-swipe.jar"]
