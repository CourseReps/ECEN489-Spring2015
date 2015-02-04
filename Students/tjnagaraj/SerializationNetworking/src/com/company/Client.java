package com.company;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Nagaraj on 2/4/2015.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", Server.PORT);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        Cars car1 = new Cars();
        car1.setMake("Nissan");
        car1.setModel("Sentra");
        car1.setYear(2014);
        objectOutputStream.writeObject(car1);
        socket.close();
    }
}