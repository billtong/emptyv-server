# empty-server

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

## Start with MAVEN (suggested)

Download [Maven](https://maven.apache.org/download.cgi). Set the global environment on the local computer.(You can find the tutorial by searching it)  
Run the script below in the terminal under the root filepath of this project.

```
mvn install
mvn tomcat7:run
```

Enter the URL `http://localhost:8080` in the browser.
Now you you can view `The Empty Video Server Author: emptyvideo team>` text on the screen. 

## Start with Tomcat7&Eclipse Jee

Download [tomcat7](https://tomcat.apache.org/download-70.cgi) and [Eclipse Jee](https://www.eclipse.org/downloads/packages/release/kepler/sr2/eclipse-ide-java-ee-developers) on the local computer.
Setting the tomcat Server on the Eclipse Jee. You can then run this project on local server by clicking 'run as' button and choose the tomcat server.  
The changes to the files will be reloaded automatically by Eclipse Jee.
