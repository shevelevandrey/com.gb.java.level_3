package com.gb.lesson_4;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private String currentLetter = "A";
    private int replayCount = 3;
    private int timeout = 500;

    public static void main(String[] args) {
        Logger logger = new Logger();
        List<Thread> threads = new ArrayList<>();

        threads.add(new Thread(() -> logger.printC()));
        threads.add(new Thread(() -> logger.printA()));
        threads.add(new Thread(() -> logger.printB()));

        threads.stream().forEach(thread -> thread.start());

        threads.stream().forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void printA() {
        for (int i = 0; i < replayCount; i++) {
            while (!currentLetter.equals("A")) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print(currentLetter);
            currentLetter = "B";
            notifyAll();
        }
    }

    public synchronized void printB() {
        for (int i = 0; i < replayCount; i++) {
            while (!currentLetter.equals("B")) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print(currentLetter);
            currentLetter = "C";
            notifyAll();
        }
    }

    public synchronized void printC() {
        for (int i = 0; i < replayCount; i++) {
            while (!currentLetter.equals("C")) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print(currentLetter);
            currentLetter = "A";
            notifyAll();
        }
    }
}
