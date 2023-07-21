package org.chatApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.sql.*;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ChatServer server;
    private final Connection connection;
    private PrintWriter out;

    public ClientHandler(Socket socket, ChatServer server, Connection connection) {
        this.clientSocket = socket;
        this.server = server;
        this.connection = connection;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String username, password;
            do {
                out.println("Enter your username: ");
                username = in.readLine();
                out.println("Enter your password: ");
                password = in.readLine();
            } while (!authenticate(username, password));

            String message;
            while ((message = in.readLine()) != null) {
                broadcast(username + ": " + message);
            }

        } catch (IOException | SQLException e) {
            System.out.println("[*] Connection from " + clientSocket.getInetAddress() + " was lost.");
        } finally {
            server.removeClient(clientSocket);
        }
    }

    private boolean authenticate(String username, String password) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            return rs.next();
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
