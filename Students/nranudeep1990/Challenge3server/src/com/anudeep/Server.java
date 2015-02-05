package com.anudeep;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {
        int clientNumber = 1;
        Scanner inp = new Scanner(System.in);
        System.out.println("Please enter the Port to bound:");
        int port = inp.nextInt();
        URL whatismyip = new URL("http://checkip.amazonaws.com/");
        BufferedReader inx = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = inx.readLine();
        ServerSocket listener = new ServerSocket(port);
        System.out.println("Server is running with \nPPP IP: "+ip +" \nlocal IP: "+Inet4Address.getLocalHost().getHostAddress() +" \non PORT: "+listener.getLocalPort());
        try {
            while(true) {
                new ServerThread(listener.accept(), clientNumber++).start();
            }

        }
        finally{
            System.out.println("ServerSocket on port "+port+" has been closed");
            inp.close();
            listener.close();
        }
    }

    private static class ServerThread extends Thread {

        private Socket socket;
        private int clientNumber;
        private StoreData storeData = new StoreData();


        public ServerThread(Socket socket,int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
        }

        public void run() {

            ClientInfo clientInfo = new ClientInfo();

            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream  objectInput = new ObjectInputStream(socket.getInputStream());

                out.println("Hello Welcome on board, you are client number " + this.clientNumber);
                out.println("Enter a line with only a period to quit\n");

                clientInfo = (ClientInfo)objectInput.readObject();

                storeData.insertData(clientInfo);

                while (true) {
                    String input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    out.println(clientInfo.getUserName()+": "+input);
                }

                in.close();
                out.close();
                objectInput.close();
                objectOut.close();
            }
            catch(IOException e) {
                e.printStackTrace();
                System.out.println("Closing the Client socket connection of "+clientNumber+" due to Stream Error");
                try {
                    socket.close();
                }
                catch(IOException e1) {
                    System.out.println("Exception in closing socket of " + clientNumber);
                }
            }
            catch(ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Closing the Client socket connection of "+clientNumber+" because of ClassNotFound");
                try {
                    socket.close();
                }
                catch(IOException e1) {
                    System.out.println("Exception in closing socket of " + clientNumber);
                }
            }
            finally {
                System.out.println("Closing the Client socket connection of "+clientNumber);
                try {
                    socket.close();
                }
                catch(IOException e1) {
                    System.out.println("Exception in closing socket of " + clientNumber);
                }
            }

        }

    }
}

