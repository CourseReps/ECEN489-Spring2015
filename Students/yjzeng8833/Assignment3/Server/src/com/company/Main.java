package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        int portNumber = 4445;
        try{
            ServerSocket server = new ServerSocket(portNumber, 1);
            System.out.println("Server is up and running, waiting for client to connect");
            Socket connection = server.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ReceiveMessage = "a";
            while(ReceiveMessage!=null){
                ReceiveMessage = bufferedReader.readLine();
                if (ReceiveMessage==null){
                    System.out.println("Client close the connection");
                }
                else
                {
                    System.out.println("Message from client: "+ReceiveMessage);
                }
            }
            connection.close();
        }catch(IOException e){
            System.out.println("exception " + e);
            System.exit(-1);
        }
    }
}
