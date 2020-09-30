package com.gb.lesson_2.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientApplicationOne {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("-auth l1 2222");
            new Thread(() -> {
                while (!socket.isClosed()) {
                    try {
                        String message = in.readUTF();
                        System.out.println(message);
                        if (message.contains("Incorrect credentials")) {
                            out.writeUTF("-auth l1 1111");
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
