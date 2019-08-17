FROM openjdk:8-jdk-alpine
EXPOSE 8002

ADD /target/user-service-1.0.jar /usr/app/user-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/user-service-1.0.jar"]
