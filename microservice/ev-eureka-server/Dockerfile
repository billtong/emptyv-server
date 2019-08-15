FROM openjdk:8-jdk-alpine
EXPOSE 8000
ADD /target/eureka-server-1.0.jar /usr/eureka-server-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/eureka-server-1.0.jar"]