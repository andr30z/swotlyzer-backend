FROM openjdk:17-alpine
VOLUME /tmp
ARG JAR_FILE=/target/auth-user-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} auth-user-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/auth-user-service.jar"]