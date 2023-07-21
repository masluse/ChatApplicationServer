# ChatApplicationServer

This project is a simple chat server built on the Java socket programming model. It can handle multiple clients at a time, distributing messages among them.

## Prerequisites

- Java 17
- Maven
- Docker (optional)

## Building and Running

### Without Docker

Build the project using Maven:

``` bash
mvn clean package
```

Run the created JAR:

``` bash
java -jar target/ChatApplicationServer-1.0-SNAPSHOT.jar
```

### With Docker

Create a Docker image:

``` bash
docker build -t my-java-app .
```

Run the Docker image:

``` bash
docker run -p 8080:8080 my-java-app
```

The application is running on port 8080.

## Functionality

- Accepts connections from multiple clients via sockets.
- Receives messages from clients and distributes them to all connected clients.
- Removes clients when they disconnect.

## License

This project is unlicensed. You can use it for any project you want, commercial or not.