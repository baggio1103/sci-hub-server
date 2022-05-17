FROM openjdk:11
WORKDIR /app
COPY /target/sci-hub.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "sci-hub.jar"]