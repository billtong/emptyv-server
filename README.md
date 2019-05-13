# empty-video-server-1.3.0

EmptyVideo server configuration guide

## MySQL configuration

Download [mysql](https://www.mysql.com/downloads/) DB on the local computer. MySql 5.7 is recommanded.
You should run scripts below under the filepath `/src` for initialization. 

```
mysql -u <yourusername> -p <yourpassword>
create empty_db;
\. <absolute filepath to initTables.sql>
\. <absolute filepath to initData.sql>
```

Set the username and password same as the those in `/src/mesources/db.properties` file. 

```
create user "tong"@"localhost" identified by "tzy990226"；
grant ALL ON empty_db.* to "tong@localhost";
flush privileges;
```

You can manually add/modify data by using [SQLyog](https://github.com/webyog/sqlyog-community/wiki/Downloads). You are all set for DB now.

##Redis configuration  
??

## Run on localhost

Download [Maven](https://maven.apache.org/download.cgi). Set the global environment on the local computer.(You can find the tutorial by searching it)  
Run the script below in the terminal under the root filepath of this project.

```
mvn clean package
java -jar target/empty-video-server.jar
```

done(there is no hot load features yet)