FROM eclipse-temurin:24-jdk-alpine as builder

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]