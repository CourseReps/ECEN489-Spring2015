package com.company;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    private void startServer() throws IOException{

        //Create a UUID for SPP
        UUID uuid = new UUID("1101", true);
        //Create the servicve url
        String connectionString = "btspp://localhost:" + uuid +";name=Sample SPP Server";

        //open server url
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

        //Wait for client connection
        System.out.println("\nServer Started. Waiting for clients to connect...");
        int count = 0;
        while(true) {
            StreamConnection connection=streamConnNotifier.acceptAndOpen();
            ServerImplementation serverImplementation = new ServerImplementation(connection);
            Thread serverimpl = new Thread(serverImplementation);
            serverimpl.start();
            count++;
        }


    }

    public static void main(String[] args) throws IOException{

        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("PB Address: "+localDevice.getBluetoothAddress());
        System.out.println("PB Name   : "+localDevice.getFriendlyName());

        Main runServer = new Main();
        runServer.startServer();

    }
}
