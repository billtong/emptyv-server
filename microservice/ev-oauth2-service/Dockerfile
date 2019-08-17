FROM openjdk:8-jdk-alpine
EXPOSE 8011

ADD /target/oauth2-service-1.0.jar /usr/app/oauth2-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/oauth2-service-1.0.jar"]
