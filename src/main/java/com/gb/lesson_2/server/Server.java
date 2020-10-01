package com.gb.lesson_2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private Set<ClientHandler> clientHandlers;
    private ClientServices clientServices;

    public Server() {
        this.clientHandlers = new HashSet<>();
        this.clientServices = new ClientServices();
        start(8888);
    }

    public ClientServices getClientServices() {
        return clientServices;
    }

    private void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            listenClients(serverSocket);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during server start-up");
        }
    }

    private void listenClients(ServerSocket serverSocket) throws IOException {
        while (true) {
            System.out.println("Server is looking for a client...");
            Socket client = serverSocket.accept();
            System.out.println("Client accepted: " + client);
            new ClientHandler(client, this);
        }
    }

    public void broadcast(ClientHandler clientHandler, String recipient, String incomingMessage) {
        if (recipient == null) {
            clientHandler.sendMessage(incomingMessage);
        } else {
            for (ClientHandler ch : clientHandlers) {
                if (ch.getClient().getName().equals(recipient)) {
                    ch.sendMessage(incomingMessage);
                }
            }
        }
    }

    public void subscribe(ClientHandler client) {
        clientHandlers.add(client);
    }

    public void unsubscribe(ClientHandler client) {
        clientHandlers.remove(client);
    }

}
