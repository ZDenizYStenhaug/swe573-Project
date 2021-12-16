
FROM openjdk:17
EXPOSE 8080
ADD /target/akitaBackend-0.0.2.jar akitaBackend-0.0.0.jar
ENTRYPOINT ["java","-jar","/akitaBackend-0.0.0.jar"]