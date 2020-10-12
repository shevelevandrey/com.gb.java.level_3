package com.gb.lesson_3.client;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private Client client;

    public GUI(String endpointHost, int endpointPort) {
        setTitle("Dummy Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(new Rectangle(0, 0, 300, 500));
        setLayout(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(chatArea, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());

        JTextField inputField = new JTextField();
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String message = inputField.getText();
            if (!message.trim().isBlank()) {
                client.sendMessage(message);
                inputField.setText("");
            }
        });

        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(submitButton, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        client = new Client(endpointHost, endpointPort, chatArea);

        setVisible(true);
    }
}
