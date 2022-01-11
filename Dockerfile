# build stage
FROM maven:3.8.4-openjdk-17 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

# package stage
FROM openjdk:17
COPY --from=build /usr/src/app/target/akitaBackend-0.0.9.jar /usr/app/akitaBackend-0.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/akitaBackend-0.0.0.jar"]
