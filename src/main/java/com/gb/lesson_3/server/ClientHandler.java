package com.gb.lesson_3.server;

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
            this.socket.setSoTimeout(36000);
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

                        sendMessage(String.format("[Server]: %s authok.", client.getName()));

                        System.out.println("Client auth completed.");
                        server.subscribe(this);

                        server.getClientServices().updateSessionStatus(client, ClientServices.authorized);
                        return;
                    } else {
                        sendMessage(String.format("[Server]: %s already logged in.", client.getName()));
                    }
                } else {
                    sendMessage("[Server]: Incorrect credentials.");
                }
            }
        }
    }

    public void closeConnection() {
        server.unsubscribe(this);
        sendMessage(String.format("[Server]: %s left chat.", (client != null) ? client.getName() : "You"));

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

            String template = "[%s]: %s";
            String formatterMessage = String.format(template, client.getName(), message);
            System.out.println(formatterMessage);

            String recipient = null;

            if (message.equalsIgnoreCase("-exit")) {
                return;
            }
            if (message.contains("-user")) {
                String[] splitMessage = message.split("\\s");
                recipient = splitMessage[1];
                formatterMessage = String.format(template, client.getName(),
                        message.replace(splitMessage[0] + " " + splitMessage[1] + " ", ""));
            }
            if (message.contains("-rename")) {
                String[] splitMessage = message.split("\\s");
                server.getClientServices().renameClientLogin(client, splitMessage[1]);

                formatterMessage = String.format("[Server]: Your login has been changed to \"%s\".", splitMessage[1]);
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
