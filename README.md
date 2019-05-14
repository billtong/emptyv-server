# empty-video-server-1.3.0

## About Empty Video Server 1.3  
The further goal of empty video are building in microservices and high level CI/CD. In order to achieve the goal, pivoting from SSM to Spring-Boot, Go and Node.js are necessary approaches. In 1.3.x, we will spend the most effort on rearranging the service layer towards 
loose coupling modules. 

## EmptyVideo server configuration guide.  

### Java & Maven Configuration  
https://wiki.jikexueyuan.com/project/maven/environment-setup.html


### MySQL configuration  
https://blog.csdn.net/sinat_35821285/article/details/86093121

### Redis configuration  
https://www.jianshu.com/p/e16d23e358c0

### Run on localhost
Run the script below under the project root folder. Using the powerful feature of Eclipse J2EE or Intellij is recommanded.

```
mvn clean package
java -jar target/empty-server-1.3.0.jar
```


### Deploy on Server
docker and jenkins for CI/CD on the way