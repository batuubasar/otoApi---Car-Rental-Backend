# Backend Dockerfile (Spring Boot)

# JDK 22 base image
FROM eclipse-temurin:22-jdk-alpine

# Working directory
WORKDIR /app

# Copy the Maven build output into the container
COPY target/otoApi-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
