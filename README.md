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
3. To pull the image and start the containers, run:

```docker compose up -d```

### Installation (from project repo)

1. Clone project repository main branch.
2. Insert the API Key into the necessary locations. *
3. To build the project, create the images and start the containers, at the root of the project, run:
```docker compose up -d. ```
4. The application can be reached here: localhost:8080

*PLEASE REFER TO THE API KEY GIVEN IN THE INSTALLATION MANUAL.

The locations are: 
- edu/boun/yilmaz4/deniz/akitaBackend/service/GeoLocationService.java, line 24.
- src/main/resources/templates/profile-page.html, line 83.
- src/main/resources/templates/view-event.html, line 91.
- src/main/resources/templates/view-offer.html, line 106.
