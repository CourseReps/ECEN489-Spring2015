package com.company;

import com.nagaraj.challenge5android.ClientInfoPacket;
import com.nagaraj.challenge5android.ClientOutMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by NAGARAJ on 2/16/2015.
 */
public class Client {

        public static void main(String[] args) throws IOException, InterruptedException {
            Scanner in =new Scanner(System.in);
            System.out.println("Please enter the IP address of Server.");
            String ipAddress= in.next();
            System.out.println("Please enter the PORT number of Server.");
            int portNumber=in.nextInt();
            Socket socket = new Socket(ipAddress, portNumber);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ClientInfoPacket clientInfoPacket = new ClientInfoPacket();
            System.out.println("Please Enter Client's name: ");
            clientInfoPacket.setClientName(in.next());
            clientInfoPacket.setMode(true);
            objectOutputStream.writeObject(clientInfoPacket);

            ClientOutMessage clientOutMessage=new ClientOutMessage();
            clientOutMessage.X=83.23;
            clientOutMessage.Y=334.45;
            clientOutMessage.Z=234.23;
            clientOutMessage.id=9;
            clientOutMessage.date="02 16 2015 2 30 PM";
            objectOutputStream.writeObject(clientOutMessage);
            clientOutMessage.id++;
            objectOutputStream.writeObject(clientOutMessage);
            clientOutMessage.id++;
            objectOutputStream.writeObject(clientOutMessage);
            Thread.sleep(10000);
            clientOutMessage.id++;
            objectOutputStream.writeObject(clientOutMessage);
            clientOutMessage.id++;
            objectOutputStream.writeObject(clientOutMessage);

            System.out.println("Client details passed to Server! ");
            socket.close();
        }
    }

