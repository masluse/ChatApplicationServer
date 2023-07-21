package org.chatApplication;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        ChatServer server = new ChatServer();
        server.start(port);
    }
}