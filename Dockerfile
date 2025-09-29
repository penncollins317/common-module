FROM eclipse-temurin:21-jre-alpine
LABEL authors="Echo"
WORKDIR /app
COPY starter/build/libs/starter-0.1.1.jar starter.jar
VOLUME ["/data/filestore"]
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "starter.jar"]