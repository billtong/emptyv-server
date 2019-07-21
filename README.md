# Empty-Video Microservices
### how to start this service
1. install docker, maven
2. run docker containers (includes mongodb & admin-mongo & zookeeper & kafka)   
If your are using docker toolbox (old docker version) and you want to use `localhost` instead of the `$(your docker-machine ip)`, you also need to do the port mapping. click the link below to learn    
[How to Forward Ports to a Virtual Machine](https://www.howtogeek.com/122641/how-to-forward-ports-to-a-virtual-machine-and-use-it-as-a-server/)  
If you don't understand the method above your can also change all application.yml files from `localhost` to `$(your docker-machine ip)`
```bash
docker-compose up -d
```
3. start all micro-services manully (will be included in docker-compose later)  
```
mvn spring-boot:run
```
### (optional) how to setup admin-mongo dashbaord
- for WinOS(old version docker only), if you have already forward the port in virtual box, then just enter `http://localhost:8082` or check the default machine IP address, open browser enter `http://your-docker-machine-ip:8082/`. 
- for other os(macos/linux), open browser enter `http://localhost:8082/`
- fill in MongoDB Connections form  
    
connName | conStr | connOpt
--- | --- | ---
(whatever) | mongodb://mongo:27017 | {}
