FROM openjdk:8-jdk-alpine
EXPOSE 8000
ADD /target/demo-0.0.1-SNAPSHOT.jar /usr/demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/usr/demo-0.0.1-SNAPSHOT.jar"]