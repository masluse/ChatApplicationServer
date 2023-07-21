# ChatApplicationServer

Dieses Projekt ist ein einfacher Chat-Server, der auf dem Java-Socket-Programmiermodell basiert. Es kann mehrere Clients gleichzeitig verwalten und Nachrichten zwischen ihnen austauschen.

## Vorraussetzungen

- Java 17
- Maven
- Docker (optional)

## Bauen und Ausführen

### Ohne Docker

Bauen Sie das Projekt mit Maven:

``` bash
mvn clean package
```

Führen Sie das erstellte JAR aus:

``` bash
java -jar target/ChatApplicationServer-1.0-SNAPSHOT.jar
```

### Mit Docker

Erstellen Sie ein Docker-Image:

``` bash
docker build -t my-java-app .
```

Führen Sie das Docker-Image aus:

``` bash
docker run -p 8080:8080 my-java-app
```

Die Anwendung läuft auf dem Port 8080.

## Funktionalitäten

- Akzeptiert Verbindungen von mehreren Clients über Sockets.
- Empfängt Nachrichten von Clients und verteilt sie an alle verbundenen Clients.
- Entfernt Clients, wenn sie die Verbindung trennen.

## Weiterentwicklung

Es gibt viele Möglichkeiten, dieses Projekt zu erweitern. Einige Ideen könnten sein:

- Implementierung von Chat-Räumen, sodass Nachrichten nur an bestimmte Gruppen von Clients gesendet werden.
- Hinzufügen einer Authentifizierung, um private Nachrichten zu ermöglichen.
- Verbesserung der Fehlerbehandlung und Stabilität des Servers.

## Lizenz

Dieses Projekt ist lizenzfrei. Sie können es für jedes Projekt verwenden, das Sie möchten, ob kommerziell oder nicht.