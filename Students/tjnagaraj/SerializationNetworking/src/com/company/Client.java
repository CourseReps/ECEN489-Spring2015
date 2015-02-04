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
        Cars car1 = new Cars();
        System.out.println("Enter the details of car to be transported over the network. ");

        System.out.println("Make of the car:");
        car1.setMake(in.next());
        System.out.println("Model of the car:");
        car1.setModel(in.next());
        System.out.println("Year of release:");
        car1.setYear(in.nextInt());
        objectOutputStream.writeObject(car1);

        System.out.println("Car details passed to Server! ");
        socket.close();
    }
}