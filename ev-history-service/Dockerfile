FROM openjdk:8-jdk-alpine
EXPOSE 8009

ADD /target/history-service-1.0.jar /usr/history-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/history-service-1.0.jar"]
