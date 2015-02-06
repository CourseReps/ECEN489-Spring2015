/**
 * Created by PRANAY KUMAR on 2/5/2015.
 */

package com.company;

import java.io.*; // ioexception and objectoutputstream
import java.net.*;
import java.util.*;// Date and Scanner


public class ClientPranay {

    public static void main(String[] args) {
        // write your code here

        Scanner cmd_input = new Scanner(System.in);


        try {
            System.out.println("Please enter the IP address of Server.");
            String ip = cmd_input.next();

            System.out.println("Please enter the PORT number of Server.");
            int portNumber = cmd_input.nextInt();

            Socket clientSocket = new Socket(ip, portNumber);

            ObjectOutputStream clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());

            // Client System properties are being transferred to server
            Date currentDate = new Date();



            clientOutput.writeObject(currentDate.toString()+"\n");
            clientOutput.writeObject("java class path : " + System.getProperty("java.class.path") + "\n");
            clientOutput.writeObject("JRE Version : " + System.getProperty("java.version") + "\n");
            clientOutput.writeObject("JRE Vendor : " + System.getProperty("java.vendor") + "\n");
            clientOutput.writeObject("Operating System Architecture : " + System.getProperty("os.arch") + "\n");
            clientOutput.writeObject(" Operating System Name: " + System.getProperty("os.name") + "\n");
            clientOutput.writeObject(" Operating System Version: " + System.getProperty("os.version") + "\n");
            clientOutput.writeObject(" User Home Directory: " + System.getProperty("user.home") + "\n");
            clientOutput.writeObject(" User Account Name: " + System.getProperty("user.name") + "\n");

            // closing connections first io objects have to be closed and then the socket

            clientOutput.close();
            clientSocket.close();


        }
        catch ( InputMismatchException e1)
        {

            System.err.printf("\nException: %s\n", e1);
            cmd_input.nextLine(); // discarding input for the user to try again
            System.out.println("You must enter Port number. Please try again.\n");

        }

        catch ( IOException e2)
        {
            System.err.println("Couldn't get I/O for the connection to: hostname \n");
        }

    }
}
