# Use the official Maven image to build the project
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the project and create the jar file
RUN mvn clean package -ntp -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:24-ea-21-slim-bookworm

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=builder /app/target/ecommerce-0.0.1-SNAPSHOT.jar rekha-server.jar

RUN ls

# Set the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "rekha-server.jar"]

# Expose the application port
EXPOSE 4444
