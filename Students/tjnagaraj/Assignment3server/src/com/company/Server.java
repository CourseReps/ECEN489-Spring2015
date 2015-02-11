package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Nagaraj on 2/4/2015.
 */
public class Server {
    public static final int PORT = 9000;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Server().runServer();
    }

    public void runServer() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("\nServer is up! \n\nServer IP address: " + InetAddress.getLocalHost().getHostAddress()+"\nServer PORT number: " + PORT );
        System.out.println("\nWaiting for incoming connections...");
        Socket socket = serverSocket.accept();
        System.out.println("\nClient connected!\n");
        ObjectInputStream objectInputStream =new ObjectInputStream(socket.getInputStream());
        System.out.println("Properties of the Client's system : \n");
        SystemProperties systemProperties= (SystemProperties) objectInputStream.readObject();
        systemProperties.printSystemProperties();
        socket.close();
    }
}
