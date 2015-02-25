package com.company;

public class Main {

    public static void main(String[] args) {

        Thread connThread = new Thread(new ConnThread());
        connThread.start();
    }
}
