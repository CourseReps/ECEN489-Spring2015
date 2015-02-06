package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Nagaraj on 2/3/2015.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Scanner in =new Scanner(System.in);
        System.out.println("Please enter the IP address of Server.");
        String ipAddress= in.next();
        System.out.println("Please enter the PORT number of Server.");
        int portNumber=in.nextInt();
        Socket socket = new Socket(ipAddress, portNumber);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        SystemProperties systemProperties = new SystemProperties();
        objectOutputStream.writeObject(systemProperties);
        System.out.println("System Properties passed to Server! ");
        socket.close();
    }
}