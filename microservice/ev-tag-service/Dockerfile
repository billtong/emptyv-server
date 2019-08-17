FROM openjdk:8-jdk-alpine
EXPOSE 8006

ADD /target/tag-service-1.0.jar /usr/app/tag-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/tag-service-1.0.jar"]
