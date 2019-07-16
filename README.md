# Empty-Video Microservices
## APIs
### user-service
- POST(/auth/login): for user to sign in, and return their public user info
- GET(/auth/user): for other services to check bearer token, and returns public user info
- GET(/auth/active/{sessionID}): for new user to activated their account via e-mail 
- GET(/api/user/{id}): for other services and user to get public info, return public user info
- POST(/api/user): for new user to register a new account, send email to the account with the activated link.
- PATCH(/api/user): for other services and user to change their profile or system info.
- ...
### asset-service
- GET(/api/comment/{id})
- GET(/api/comment/video/{id})
- PATCH(/api/commment/{id}/like)
- POST(/api/comment")
- DELETE(/api/comment/{id})
- ...
### consumer-service
- GET(/api/notification/{userId})
- ...
## Deploy
### how to start this service （will be replaced by Jenkins）
1. install docker, maven
2. run docker containers (mongodb & admin-mongo/dashboard)
```bash
docker-compose up -d
```
3. start all three micro-services manully.  
[empty-video-asset-service](https://github.com/naglfari/empty-video-asset-service.git)  
[empty-video-user-service](https://github.com/naglfari/empty-video-user-service.git)  
[empty-video-consumer-service](https://github.com/naglfari/emptyvideo-consumer-service.git)  
```
mvn spring-boot:run
```
still writing jenkins pipeline

### how to setup mongodDB dashbaord
- for WinOS(old version docker only), first check the default machine IP address, open browser enter `http://docker-machine-ip:8082/`, don't forget to change application.yml file as well.
- for other os(macos/linux), open browser enter `http://localhost:8082/`
- fill in MongoDB Connections form  
    
connName | conStr | connOpt
--- | --- | ---
(whatever) | mongodb://mongo:27017 | {}

## some learning links
[Spring Web Flux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)  
[Spring Data Mongo Reactive](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)  





