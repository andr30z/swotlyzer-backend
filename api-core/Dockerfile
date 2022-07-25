FROM openjdk:11.0.9-jdk-slim
ARG JAR_FILE=target/api-core.jar
COPY ${JAR_FILE} api-core.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/api-core.jar"]