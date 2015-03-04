package com.company;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.net.*;

/**
 * Created by pranay_000 on 2/22/2015.
 */


public class NotoriousServer {


    public static void main(String[] args) throws IOException
    {
        Socket clientSocket = null;
        ServerSocket server = null;

        int port = 9898;
        System.out.println("The Notorious server is running.");
        System.out.println("Server IP address:" + InetAddress.getLocalHost().getHostAddress() + "\nServer PORT number:9898");
        server = new ServerSocket(port);

        try {
            //Wait for client to connect
            while (true) {
                System.out.println("Waiting for client connection...");
                clientSocket = server.accept();
                System.out.println("Client Connected!");
                MuleData writeDB = new MuleData(clientSocket);
                Thread dbWriteThread = new Thread(writeDB);
                dbWriteThread.start();
            }

        }
        catch(IOException ex){
            System.out.println("Could not establish server socket on port " + port);
            ex.printStackTrace();
            server.close();
            System.exit(0);
        }

        finally {
            server.close();
            System.exit(0);
        }

    }


}

