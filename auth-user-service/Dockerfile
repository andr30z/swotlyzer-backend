FROM openjdk:11.0.9-jdk-slim
ARG JAR_FILE=target/auth-user-service.jar
COPY ${JAR_FILE} auth-user-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/auth-user-service.jar"]