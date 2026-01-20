FROM eclipse-temurin:25-jre-alpine
LABEL authors="Echo"
WORKDIR /app
COPY starter/build/libs/starter-0.3.0.jar starter.jar
VOLUME ["/data/filestore"]
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar starter.jar"]