package com.gb.lesson_2.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientApplicationSendMessages {
    public static void main(String[] args) {
        List<Map<String, String>> messageList = new ArrayList<>();

        Map<String, String> messageBox1 = new HashMap<>();
        messageBox1.put("user", "u1");
        messageBox1.put("message", "Hello, user1!");
        messageBox1.put("is_send", "");
        messageList.add(messageBox1);

        Map<String, String> messageBox2 = new HashMap<>();
        messageBox2.put("user", "u2");
        messageBox2.put("message", "Hello, user2!");
        messageBox2.put("is_send", "");
        messageList.add(messageBox2);

        try {
            Socket socket = new Socket("localhost", 8888);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("-auth l4 3333");
            new Thread(() -> {
                while (true) {
                    try {
                        String message = in.readUTF();
                        System.out.println(message);
                        if (message.contains("Incorrect credentials")) {
                            out.writeUTF("-auth l4 4444");
                        }

                        // send messages
                        for (Map<String, String> m : messageList) {
                            if (m.get("is_send").isEmpty()) {
                                out.writeUTF(String.format("-user %s %s", m.get("user"), m.get("message")));
                                m.put("is_send", "1");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                        try {
                            in.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                        return;
                    }
                }
            }).start();

            out.writeUTF("-exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
