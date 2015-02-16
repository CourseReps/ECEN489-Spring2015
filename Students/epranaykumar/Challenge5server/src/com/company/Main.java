package com.company;

import java.io.*; // ioexception and objectoutputstream
import java.net.*;


public class Main {

    public static void main(String[] args) {

        // write your code here

        try {
            ServerSocket server = new ServerSocket(6012);
            System.out.println("Server IP address:" + InetAddress.getLocalHost().getHostAddress() + "\nServer PORT number:6012");
            System.out.println("Waiting for client connection to this port");

            Socket socket = server.accept();

            System.out.println("\nClient connected!\n");
            ObjectInputStream serverInput =new ObjectInputStream(socket.getInputStream());
            do {
                System.out.printf("id:%s \n", serverInput.readObject());
                System.out.printf("x:%s", serverInput.readObject());
                System.out.printf("y:%s", serverInput.readObject());
                System.out.printf("z:%s \n", serverInput.readObject());

            }while(serverInput.readObject().equals("RUN"));


            System.out.println("Data received from client: ");

            serverInput.close();
            socket.close();




        }
        catch (UnknownHostException e) {
            System.err.println("ip address error");
        } catch (IOException e1) {
            System.err.println("Couldn't get I/O connection ");
        }
        catch (ClassNotFoundException e1) {
            System.err.println("Couldn't get I/O connection ");
        }

    }
}
