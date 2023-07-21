package org.chatApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private ChatServer server;

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                broadcast(message);
            }

            removeClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        ArrayList<Socket> clients = server.getClients();
        for (Socket client : clients) {
            try {
                PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
                clientOut.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeClient() {
        server.removeClient(clientSocket);
    }
}

