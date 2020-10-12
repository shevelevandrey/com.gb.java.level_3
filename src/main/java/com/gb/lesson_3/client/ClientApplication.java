package com.gb.lesson_3.client;

public class ClientApplication {
    public static void main(String[] args) {
        new Thread(() -> new GUI("localhost", 8888)).start();
        new Thread(() -> new GUI("localhost", 8888)).start();
    }
}
