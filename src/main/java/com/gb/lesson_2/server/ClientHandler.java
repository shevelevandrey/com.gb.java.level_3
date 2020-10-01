package com.gb.lesson_2.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;

/**
 * Represents client session
 */
public class ClientHandler {
    private ClientServices.Client client;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.socket.setSoTimeout(12000);
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.server = server;
    }

    public ClientServices.Client getClient() {
        return client;
    }

    public void start(){
        new Thread(() -> {
            try {
                authenticate();
                readMessage();
            } catch (SocketTimeoutException e) {
                System.out.printf("Time expired for unauthorized client: %s.%n", socket);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }).start();
    }

    public void authenticate() throws IOException {
        System.out.println("Client auth is on going...");
        while (true) {
            String loginInfo = in.readUTF();
            if (loginInfo.startsWith("-auth")) {
                // -auth l1 p1
                String[] splittedLoginInfo = loginInfo.split("\\s");
                client = server.getClientServices()
                        .findClient(
                                splittedLoginInfo[1],
                                splittedLoginInfo[2]
                        );
                if (client != null) {
                    if (!server.getClientServices().isClientAuthorized(client)) {
                        socket.setSoTimeout(0);
                        sendMessage(String.format("Status: %s authok.", client.getLogin()));

                        server.broadcast(this, "", String.format("%s came in.", client.getName()));
                        System.out.println("Client auth completed.");
                        server.subscribe(this);

                        server.getClientServices().updateSessionStatus(client, ClientServices.authorized);
                        return;
                    } else {
                        sendMessage(String.format("%s already logged in.", client.getName()));
                    }
                } else {
                    sendMessage("Incorrect credentials.");
                }
            }
        }
    }

    public void closeConnection() {
        server.unsubscribe(this);
        server.broadcast(this, "", String.format("%s left chat", (client != null) ? client.getName() : "You"));

        if (client != null) {
            server.getClientServices().updateSessionStatus(client, ClientServices.notAuthorized);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessage() throws IOException {
        while (true) {
            String message = in.readUTF();
            String formatterMessage = String.format("Message from %s: %s", client.getName(), message);
            System.out.println(formatterMessage);

            String recipient = null;

            if (message.equalsIgnoreCase("-exit")) {
                return;
            }
            if (message.contains("-user")) {
                String[] splittedMessage = message.split("\\s");
                recipient = splittedMessage[1];
                formatterMessage = String.format("Message from %s: %s", client.getName(),
                        message.replace(splittedMessage[0] + " " + splittedMessage[1] + " ", ""));
            }
            if (message.contains("-rename")) {
                String[] splittedMessage = message.split("\\s");
                server.getClientServices().renameClientLogin(client, splittedMessage[1]);

                formatterMessage = String.format("Your login has been changed to \"%s\".", splittedMessage[1]);
            }

            server.broadcast(this, recipient, formatterMessage);
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHandler that = (ClientHandler) o;
        return Objects.equals(client, that.client) &&
                Objects.equals(socket, that.socket) &&
                Objects.equals(in, that.in) &&
                Objects.equals(out, that.out) &&
                Objects.equals(server, that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, socket, in, out, server);
    }

    @Override
    public String toString() {
        return "ClientHandler{" +
                "client='" + client + '\'' +
                ", socket=" + socket +
                ", in=" + in +
                ", out=" + out +
                ", server=" + server +
                '}';
    }
}
