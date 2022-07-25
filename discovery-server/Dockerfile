FROM openjdk:11.0.9-jdk-slim
ARG JAR_FILE=target/discovery-server.jar
COPY ${JAR_FILE} discovery-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/discovery-server.jar"]