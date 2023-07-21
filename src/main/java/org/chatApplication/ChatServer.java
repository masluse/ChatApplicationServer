package org.chatApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.*;

public class ChatServer {
    private final ConcurrentHashMap<Socket, ClientHandler> clients = new ConcurrentHashMap<>();
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port);
             Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("[*] Server listening on IP address: " + serverSocket.getInetAddress().getHostAddress() + ", port: " + port);

            // Initialize database
            initializeDatabase(connection);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[*] Accepted connection from " + clientSocket.getInetAddress());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, this, connection);
                clients.put(clientSocket, clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabase(Connection connection) throws SQLException {
        // Create users table if it doesn't exist
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (username VARCHAR(255), password VARCHAR(255))");
            statement.execute("CREATE TABLE IF NOT EXISTS messages (username VARCHAR(255), message TEXT, timestamp TIMESTAMP)");
        }

        // Insert users from USERS environment variable
        String users = System.getenv("USERS");
        for (String user : users.split(";")) {
            String[] parts = user.split(",");
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                statement.setString(1, parts[0]);
                statement.setString(2, parts[1]);
                statement.execute();
            }
        }
        
        // Run a test query to check if data can be retrieved correctly
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                System.out.println("User in database: " + rs.getString("username") + ", Password: " + rs.getString("password"));
            }
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
