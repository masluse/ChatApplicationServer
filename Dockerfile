# Build stage
FROM maven:3.8.5-openjdk-17-slim as builder

# Copy pom.xml and source code to the container
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# Package the application
RUN mvn clean package

# Run stage
FROM eclipse-temurin:17-jre-alpine

# Copy the jar file built in the first stage
COPY --from=builder /target/*.jar /app/

# Run the application
ENTRYPOINT ["java","-jar","/app/ChatApplicationServer-1.0-SNAPSHOT.jar"]
