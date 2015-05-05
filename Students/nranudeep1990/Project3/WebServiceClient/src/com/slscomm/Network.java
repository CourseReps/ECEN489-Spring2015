package com.slscomm;

/**
 * Created by PRANAY KUMAR on 4/15/2015.
 */


import java.io.*;
import java.net.Socket;

public class Network implements Runnable{

    public static String querry;
    private Socket clientSocket;

    Network (Socket socket)
    {
        this.clientSocket = socket;
    }

    public  void run() {


                  try {
                      // receive querry
                      while(true) {

                          BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                          querry = input.readLine();

                          if(querry.equals("disconnect")) {
                              clientSocket.close();
                              System.out.println("Recieved \"disconnect\" command \nClient Socket Closed  ");
                              break;
                          }
                          System.out.println("\nReceived Querry: " + querry);

                          // call processRequest Method

                          ProcessRequest Response = new ProcessRequest();
                          String clientResponse = Response.process(querry);


                          // Send back string clientResponse
                          PrintWriter wr_to_app = new PrintWriter(clientSocket.getOutputStream(), true);
                          wr_to_app.println(clientResponse);
                          wr_to_app.flush();

                          System.out.println("Sent ClientResponse: " + clientResponse);


                      }
                   }

                 catch (Exception ex) {
                           ex.printStackTrace();
                           // Do exception handling
                 }
    }


    }
