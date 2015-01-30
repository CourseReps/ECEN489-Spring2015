package com.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
/*
Main program that instantiates the serverSocket to listen for a
client and starts a separate thread for the connected client
 */
    public static void main(String[] args) throws Exception {

        System.out.println("Server is running on Port 9000....");
        int clientNumber = 0;
        //creates a listener socket on port 9000 which can have 5 clients on wait
        ServerSocket listener = new ServerSocket(9000,5);
        try {
            //infinite loop to continuously listen for new client connections
            while (true) {
                //Threading enables to connect with many clients simultaneously
                new ServerThread(listener.accept(), clientNumber++).start();
            }
        } finally {
            //shutting down the server by closing the socket
            listener.close();
        }

    }


private static class ServerThread extends Thread {
    private Socket socket;
    private int clientNumber;
/*
Constructor for the ServerThread class
 */
    public ServerThread(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        System.out.println("New connection with client# " + clientNumber + " at " + socket);
    }
/*
Logic to be implemented while thread is started from the main function
 */
    public void run() {
        try {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Hello, you are client #" + clientNumber + ".");
            out.println("Enter a line with only a period to quit\n");

            while (true) {
                String input = in.readLine();
                if (input == null || input.equals(".")) {
                    break;
                }
                out.println(input.toUpperCase());
            }
        } catch (IOException e) {
            System.out.println("Error handling client# " + clientNumber + ": " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error while closing the Socket.... Socket can not be closed");
            }
            System.out.println("Connection with client# " + clientNumber + " closed");
        }
    }


}

}
