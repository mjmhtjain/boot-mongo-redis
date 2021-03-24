#
# Build stage
#
FROM maven:3.6.3-openjdk-15-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:15-jdk-slim
COPY --from=build /home/app/target/simpledb-0.0.1-SNAPSHOT.jar /usr/local/lib/application.jar
EXPOSE 8080
ENTRYPOINT exec java -jar /usr/local/lib/application.jar
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar webfluxbasicapi.jar
