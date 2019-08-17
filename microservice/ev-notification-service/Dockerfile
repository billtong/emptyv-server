FROM openjdk:8-jdk-alpine
EXPOSE 8008

ADD /target/notification-service-1.0.jar /usr/notification-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/notification-service-1.0.jar"]
