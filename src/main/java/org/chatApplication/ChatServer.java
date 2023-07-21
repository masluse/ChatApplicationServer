package org.chatApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private final ConcurrentHashMap<Socket, ClientHandler> clients = new ConcurrentHashMap<>();

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[*] Server listening on IP address: " + serverSocket.getInetAddress().getHostAddress() + ", port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[*] Accepted connection from " + clientSocket.getInetAddress());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.put(clientSocket, clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(Socket clientSocket) {
        clients.remove(clientSocket);
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<Socket, ClientHandler> getClients() {
        return clients;
    }
}
