package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        int portNumber = 12345;
        try{
            ServerSocket server = new ServerSocket(portNumber, 1);
            System.out.println("begin");
            Socket connection = server.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String messageFromClient = bufferedReader.readLine();
            while(messageFromClient!=null){
                System.out.println("Message from client: "+messageFromClient);
                messageFromClient = bufferedReader.readLine();
            }
            connection.close();
        }catch(IOException e){
            System.out.println("exception " + e);
            System.exit(-1);
        }
    }
}
