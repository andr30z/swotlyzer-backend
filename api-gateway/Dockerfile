FROM openjdk:11.0.9-jdk-slim
ARG JAR_FILE=target/api-gateway.jar
COPY ${JAR_FILE} api-gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/api-gateway.jar"]