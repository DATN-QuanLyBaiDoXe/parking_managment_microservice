FROM adoptopenjdk/openjdk11:alpine
VOLUME /logs
VOLUME /config
EXPOSE 8484
COPY management-service-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]