FROM openjdk:8-jdk-alpine
EXPOSE 8761

ADD /target/eureka-server-1.0.jar /usr/app/eureka-server-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/eureka-server-1.0.jar"]
