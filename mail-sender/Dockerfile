FROM openjdk:17-alpine
VOLUME /tmp
ARG JAR_FILE=/target/mail-sender-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} mail-sender.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mail-sender.jar"]