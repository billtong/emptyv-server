FROM openjdk:8-jdk-alpine
EXPOSE 8000
ADD /target/api-gateway-1.0.jar /usr/app/api-gateway-1.0.jar
ENTRYPOINT ["java", "-jar", "/usr/app/api-gateway-1.0.jar"]
