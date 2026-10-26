# Multi-stage Dockerfile for TipuSkip build and test
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and database scripts
COPY src ./src
COPY database ./database

# Run unit tests and package
RUN mvn clean test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar ./tipuskip.jar
CMD ["echo", "TipuSkip Desktop Core built successfully. Run JavaFX on host desktop via 'mvn javafx:run'."]
