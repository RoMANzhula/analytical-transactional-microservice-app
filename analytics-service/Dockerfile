# stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# stage 2: Create a lightweight image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/analytics-service-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]