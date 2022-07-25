FROM openjdk:11.0.9-jdk-slim
VOLUME /tmp
ARG JAR_FILE=/target/discovery-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} discovery-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/discovery-server.jar"]