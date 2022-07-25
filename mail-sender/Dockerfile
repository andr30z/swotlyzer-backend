FROM openjdk:11.0.9-jdk-slim
ARG JAR_FILE=target/mail-sender.jar
COPY ${JAR_FILE} mail-sender.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mail-sender.jar"]