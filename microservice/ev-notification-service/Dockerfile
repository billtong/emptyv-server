FROM openjdk:8-jdk-alpine
EXPOSE 8008

ADD /target/notification-service-1.0.jar /usr/app/notification-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/notification-service-1.0.jar"]
