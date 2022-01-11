Akita is a community platform where people can offer and find services from other community members. It seeks to be an open and creative community where members can share their talents and discover new interests. It is designed to support all kinds of services and event, and to grow and develop with the community.

There is no use of money in Akita. Offers are given and taken according to the member’s time credits and the duration of the offers.

### System Requirements
- Spring Boot, Java 17
- Apache Maven 3.8.4
- Docker, docker-compose

### Installation (from docker-hub)
1. Save the code below code docker-compose.yml file in a separate folder in your host machine.
```
version: '3.7'
services:
  database:
    image: postgres
    container_name: akita-postgres
    environment:
      - POSTGRES_DB=akita
      - POSTGRES_USER=akita
      - POSTGRES_PASSWORD=4k1t4
      - TZ=Europe/Istanbul
      - PGTZ=Europe/Istanbul
    ports:
      - "5432:5432"

  akita:
    image: zeynepdyilmaz/akita
    container_name: akita-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/akita
      - SPRING_DATASOURCE_USERNAME=akita
      - SPRING_DATASOURCE_PASSWORD=4k1t4
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    links:
      - database
    depends_on:
      - database
```

2. Open a command prompt (or terminal) window and move into the folder where you saved the docker-compose.yml file.
3.	Run docker compose up -d. This will pull the image from docker hub and start the containers.
