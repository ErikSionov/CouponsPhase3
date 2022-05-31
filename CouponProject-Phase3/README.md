# Coupons Project Phase 3
this project is a dashboard backend for a coupons purchasing SPA.
Made with Spring boot and angular. API has CRUD functionality and more.

## Tech/framework used:
Spring boot, MySql DB, Swagger, Lombok, JWT, maven

## Features
1. DB is self deployed and created if schemas not present in DB using Hibernate/JPA
3. Scheduled deletion of expired coupons daily at 00:00
4. to access any API endpoint(excluding LoginManager) user must send with their request JWT authentication. Web filter prevent access to endpoints without token.
5. Lombok used to prevent boilerplate bloat.

## Installation
1. import as a Maven project to Eclipse with spring tools or spring tools suite
2. run maven to get all dependencies from the pom.xml file
4. make sure mySql DB is running on the right port and credentials are right. (config through application.properties)
5. run application as Spring Boot Application
6. the api is running, can check endpoints with swagger-ui at localhost:8080/swagger-ui.html
