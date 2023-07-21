package org.chatApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ChatServer server;
    private PrintWriter out;

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                broadcast(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(clientSocket);
        }
    }

    public void broadcast(String message) {
        for (Map.Entry<Socket, ClientHandler> entry : server.getClients().entrySet()) {
            entry.getValue().sendMessage(message);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
