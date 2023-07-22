package org.chatApplication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {
    private static final Map<String, String> userDatabase = new HashMap<>();
    private static final List<Handler> handlers = new ArrayList<>();
    private static final List<Socket> clients = new ArrayList<>();

    static {
        // Populate user database
        userDatabase.put("user1", "password1");
        userDatabase.put("user2", "password2");
        // Add more users as necessary
    }

    public static void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("\u001B[32m[*] Server started on port " + port + "\u001B[0m");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                new Handler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Socket> getClients() {
        return clients;
    }

    public static void removeClient(Socket client) {
        clients.remove(client);
    }

    private static class Handler extends Thread {
        private String name;
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Handle authentication
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    String[] credentials = input.split(":");
                    if (credentials.length == 2 && userDatabase.containsKey(credentials[0]) && userDatabase.get(credentials[0]).equals(credentials[1])) {
                        name = credentials[0];
                        out.println("Authenticated");
                        break;
                    } else {
                        out.println("Authentication failed");
                    }
                }

                synchronized (handlers) {
                    handlers.add(this);
                }

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (Handler handler : handlers) {
                        handler.out.println(name + ": " + input);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (name != null) {
                    System.out.println(name + " is leaving");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (handlers) {
                    handlers.remove(this);
                }
                removeClient(socket); // call to remove client from the client list
            }
        }
    }
}
