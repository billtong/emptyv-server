FROM openjdk:8-jdk-alpine
EXPOSE 8006

ADD /target/tag-service-1.0.jar /usr/tag-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/tag-service-1.0.jar"]
