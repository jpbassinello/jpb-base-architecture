# JPB Spring Boot Reference Architecture #

## Contents ##

* Rest APIs built with `Spring Framework`
* `Spring Data` repositories. JCache with `ehcache 3` integration to boost data fetch
* Protected `Oauth2` Endpoints with `Spring Security` and `@EnableAuthorizationServer`
* User model with social login. `Facebook` and `Google` integration.
* Enhanced log with `logback` and `Spring` integration
* Auto generated `Swagger`
* Usage of `MySQL` database 
* `QueryDSL` integration for metadata filtering queries
* `Lombok`
* Tests with `rest-assured`
* `maria-db` to have fully featured MySQL tests 

## Developer Environment (LOCALHOST) ##

### Requirements ###

- Development tool (IDE) such IntelliJ IDEA, Eclipse, Netbeans
- Java JDK 13 (http://www.oracle.com/technetwork/java/javase/downloads/)
- MySQL Server (https://dev.mysql.com/downloads/)

### Configuration ###

- Database
    - Create "jpb-base-architecture" schema on MySQL database

### Start Development Server ###

- Run spring boot main method at br.com.jpb.config.Application class
