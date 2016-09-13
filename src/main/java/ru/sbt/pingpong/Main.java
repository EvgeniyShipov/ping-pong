package ru.sbt.pingpong;


public class Main {
    private static final Object lock = new Object();
    private static boolean writePing = true;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                while (true) {
                    while (writePing) {
                        try {
                            System.out.println("PING");
                            writePing = false;
                            lock.notify();
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Thread1 is interrupted", e);
                        }
                    }
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                while (true) {
                    while (!writePing) {
                        try {
                            System.out.println("PONG");
                            writePing = true;
                            lock.notify();
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Thread2 is interrupted", e);
                        }
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
