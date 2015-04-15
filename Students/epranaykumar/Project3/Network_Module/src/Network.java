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

                      BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                      querry = input.readLine();

                      // call processor thread

                      ProcessRequest Response = new ProcessRequest();
                      String clientResponse = Response.process(querry);


                      // Send back string clientResponse
                      PrintWriter wr_to_app = new PrintWriter(clientSocket.getOutputStream(), true);
                      wr_to_app.println( clientResponse);
                      wr_to_app.flush();

                      clientSocket.close();
                   }

                 catch (Exception ex) {
                           ex.printStackTrace();
                           // Do exception handling
                 }
    }


    }
