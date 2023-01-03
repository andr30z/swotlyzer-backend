FROM openjdk:17-alpine
VOLUME /tmp
ARG JAR_FILE=/target/swot-analysis-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} swot-analysis-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/swot-analysis-service.jar"]