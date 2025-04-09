# Basic-Payment-Transfer-Service
Payment transfer application that is responsible for transfer funds between accounts that use this platform.

# Local instructions (execute next commands if you want to run locally)
- mvn clean install
- mvn spring-boot:run

# Test (execute next commands if you want to run tests)
- mvn test

# Docker instructions (execute next commands if you want to run it using docker)
- mvn jib:dockerBuild 
- docker compose up -d

Spring boot properties are defined inside application.properties.
Docker properties are defined inside docker-compose file.

Swagger can be reached on url: http://localhost:8080/swagger-ui/index.html

Postman collection for testing and presentation purposes is in resources/postman folder.

