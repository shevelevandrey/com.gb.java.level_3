package com.gb.lesson_3.client;

import com.gb.lesson_3.client.FileHandler.HistoryMessages;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Client {
    private String endpointHost;
    private int endpointPort;
    private Socket clientSocket;

    private DataInputStream in;
    private DataOutputStream out;

    JTextArea charArea;
    HistoryMessages historyMessages;

    public Client(String endpointHost, int endpointPort, JTextArea charArea) {
        this.charArea = charArea;
        this.endpointHost = endpointHost;
        this.endpointPort = endpointPort;

        listenMessages();
    }

    private void listenMessages () {
        try {
            clientSocket = new Socket(endpointHost, endpointPort);
            System.out.println("Client info: " + clientSocket);

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            historyMessages = new HistoryMessages("client_messages.txt");
            loadHistory();

            new Thread(() -> {
                try {
                    while (!clientSocket.isInputShutdown()) {
                        String message = in.readUTF();
                        displayMessage(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            displayMessage(String.format("[My]: %s%n", message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayMessage(String message) {
        charArea.append(message);
        historyMessages.writeMessage(message);
    }

    private void loadHistory() {
        List<String> history = historyMessages.getData(5);
        history.stream().forEach(s -> charArea.append(String.format("%s%n", s)));
    }

}
