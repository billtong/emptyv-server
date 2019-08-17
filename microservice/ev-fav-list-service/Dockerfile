FROM openjdk:8-jdk-alpine
EXPOSE 8005

ADD /target/fav-list-service-1.0.jar /usr/fav-list-service-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/fav-list-service-1.0.jar"]
