FROM openjdk:8-jdk-alpine
EXPOSE 8002
ADD /target/comment-service-1.0.jar /usr/app/comment-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/comment-service-1.0.jar"]
