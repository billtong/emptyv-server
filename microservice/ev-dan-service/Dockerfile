FROM openjdk:8-jdk-alpine
EXPOSE 8003
ADD /target/dan-service-1.0.jar /usr/app/dan-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/dan-service-1.0.jar"]
