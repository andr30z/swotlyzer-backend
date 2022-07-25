FROM openjdk:11.0.9-jdk-slim
VOLUME /tmp
ARG JAR_FILE=/target/api-gateway-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} api-gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/api-gateway.jar"]