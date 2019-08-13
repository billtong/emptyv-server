# EmptyVideo MicroServices
> This Project is Still Under Construction!
## how to run this service
### 1. install java 8, docker, maven, IntelliJ
### 2. run docker containers (includes mongodb & admin-mongo & zookeeper & kafka)   
If your are using docker toolbox (old docker version) and you want to use `localhost` instead of the `$(your docker-machine ip)`, you also need to do the port mapping. click the link below to learn    
[How to Forward Ports to a Virtual Machine](https://www.howtogeek.com/122641/how-to-forward-ports-to-a-virtual-machine-and-use-it-as-a-server/)  
If you don't understand the method above your can also change all application.yml files from `localhost` to `$(your docker-machine ip)`
```bash
docker-compose up -d
```
### 3. start multiple micro-services via IntelliJ
- download lombok plugin on your IntelliJ
- import the project of `ev-eureka-server` into your IntelliJ, you can then boost all the services eazily
- boost squence:
    1. ev-eureka-server
    2. user-service
    3. others
```
mvn spring-boot:run
```
## how to deploy this service
- git pull
- maven build
- docker compose
## Some helpful dashboard 4 U
### Admin-Mongo, a mongoDB dashboard
- for WinOS(old version docker only), if you have already forward the port in virtual box, then just enter `http://localhost:8082` or check the default machine IP address, open browser enter `http://your-docker-machine-ip:8082/`. 
- for other os(macos/linux), open browser enter `http://localhost:8082/`
- fill in MongoDB Connections form  
    
connName | conStr | connOpt
---: | :---: | :---:
(whatever) | mongodb://mongo:27017 | {}
### Eureka Dashboard
- open browser enter `http://localhost:8761/`