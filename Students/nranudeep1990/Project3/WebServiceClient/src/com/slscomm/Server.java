package com.slscomm;
/**
 * Created by pranay_000 on 4/14/2015.
 */


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {


    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        ServerSocket server = null;

        int port = 9898;
        System.out.println("The Server is running.");
        System.out.println("Server IP address:" + InetAddress.getLocalHost().getHostAddress() + "\nServer PORT number:9898");
        server = new ServerSocket(port);


        //Wait for client to connect



            while (true) {

                if(server.isClosed()){
                    server = new ServerSocket(port);
                }

                try {

                System.out.println("Waiting for client connection...");
                clientSocket = server.accept();
                System.out.println("Client Connected!");

                Network writeDB = new Network(clientSocket);
                Thread dbWriteThread = new Thread(writeDB);
                dbWriteThread.start();

                }

                catch (IOException ex) {
                    System.out.println("Could not establish server socket on port " + port);
                    ex.printStackTrace();
                    server.close();
                }

        }


    }


}

