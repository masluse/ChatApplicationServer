# Build stage
FROM maven:3.8.5-openjdk-17-slim as builder

# Copy pom.xml and source code to the container
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# Package the application
RUN mvn clean package

# Run stage
FROM eclipse-temurin:17-jre-alpine

# Install wget
RUN apk add --no-cache wget

# Create directory for Java extensions
RUN mkdir -p /usr/share/java

# Download PostgreSQL JDBC driver
RUN wget https://jdbc.postgresql.org/download/postgresql-42.2.14.jar -P /usr/share/java/

# Copy the jar file built in the first stage
COPY --from=builder /target/*.jar /app.jar

# Add PostgreSQL driver to classpath
ENV CLASSPATH=/usr/share/java/postgresql-42.2.14.jar:$CLASSPATH

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]
