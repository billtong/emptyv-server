# Empty-Video Microservices
## 1. APIs
### user-service [8001:]
- GET(/api/user/{id})
- POST(/api/user)
- PATCH(/api/user)
- POST(/auth/login)
- GET(/auth/user)
- GET(/auth/active/{sessionID})
- ...
### comment-service [8002:]
- GET(/api/comment/{id})
- GET(/api/comment/video/{id})
- PATCH(/api/commment/{id}/like)
- POST(/api/comment")
- DELETE(/api/comment/{id})
- ...
### dan-service [8003:]
- ...
### video-service [8004:]
- ...
### favlist-service [8005:]
- ...
### message-service [8006:]
- ...
### notification-service [8007:]
- GET(/api/notification/{userId})
- ...
### history-serivce [8009:]
- ...
### point-service [8010:]
- ...
## 2. Deploy
### how to start this service
1. install docker, maven
2. run docker containers (includes mongodb & admin-mongo & zookeeper & kafka)   
If your are using docker toolbox (old docker version) and you want to use `localhost` instead of the `$(your docker-machine ip)`, you also need to do the port mapping. click the link below to learn    
[How to Forward Ports to a Virtual Machine](https://www.howtogeek.com/122641/how-to-forward-ports-to-a-virtual-machine-and-use-it-as-a-server/)  
If you don't understand the method above your can also change all application.yml files from `localhost` to `$(your docker-machine ip)`
```bash
docker-compose up -d
```
3. start all three micro-services manully (will be included in docker-compose later)  
[empty-video-comment-service](empty-video-comment-service)  
[empty-video-user-service](empty-video-user-service)  
[empty-video-notification-service](empty-video-notification-service)  
```
mvn spring-boot:run
```
### how to setup mongodDB dashbaord
- for WinOS(old version docker only), if you have already forward the port in virtual box, then just enter `http://localhost:8082` or check the default machine IP address, open browser enter `http://your-docker-machine-ip:8082/`. 
- for other os(macos/linux), open browser enter `http://localhost:8082/`
- fill in MongoDB Connections form  
    
connName | conStr | connOpt
--- | --- | ---
(whatever) | mongodb://mongo:27017 | {}

## 3. service details
### notification serivces diagram

![ad](docs/img/ev-notification-service-flow.png)

1. POST(/api/asset): browser → asset service (handle http reqeust)
2. GET(/api/auth): asset service → user service (send http requests)
3. save asset to mongodb: asset service → MongoDB (sql operation)
4. send this operation to Kafka: asset service → Kafka (produce message)
5. receive this operation from kafka: notification service → Kafka (consume message)
6. save notification to monodb: notification service → MongoDB (sql operation)
7. send this update info to kafka: notification service → Kafka (produce message)
8. receive this update from kafka: user service → kafka (consume message)
9. learning websoket now...
## Architecture designed by Bill Tong  ╮(￣▽￣)╭ 
