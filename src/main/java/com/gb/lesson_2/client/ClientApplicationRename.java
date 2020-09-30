package com.gb.lesson_2.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientApplicationRename {
    public static void main(String[] args) {
        List<Map<String, String>> messageList = new ArrayList<>();

        Map<String, String> messageBox = new HashMap<>();
        messageBox.put("new_login", "l777");
        messageBox.put("is_send", "");
        messageList.add(messageBox);

        try {
            Socket socket = new Socket("localhost", 8888);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("-auth l7 3333");
            new Thread(() -> {
                while (true) {
                    try {
                        String message = in.readUTF();
                        System.out.println(message);
                        if (message.contains("Incorrect credentials")) {
                            out.writeUTF("-auth l7 7777");
                        }

                        // rename login
                        for (Map<String, String> m : messageList) {
                            if (m.get("is_send").isEmpty()) {
                                out.writeUTF(String.format("-rename %s", m.get("new_login")));
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
