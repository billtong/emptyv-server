FROM openjdk:8-jdk-alpine
EXPOSE 8004
ADD /target/video-service-1.0.jar /usr/app/video-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/video-service-1.0.jar"]
