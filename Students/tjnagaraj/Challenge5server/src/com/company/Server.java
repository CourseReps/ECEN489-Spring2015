    package com.company;

    import java.io.IOException;
    import java.io.ObjectInputStream;
    import java.net.Inet4Address;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.Scanner;

    public class Server {

        public static void main(String[] args) throws IOException {
            int clientNumber = 0;
            // getting the Port number from user
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter the Port number to host the Server");
            int port = in.nextInt();

            System.out.println("Server IP address :" + Inet4Address.getLocalHost());

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("The Server is up!\n \n Waiting for incoming connections...");
            SQLiteJDBC myDb = new SQLiteJDBC();
            try {

                while (true) {
                    new ServerThread(serverSocket.accept(), ++clientNumber).start();
                }
            } finally {
                System.out.println("ServerSocket bound to port" + port + " has been closed");
            }

        }
    }

        class ServerThread extends Thread {
            private Socket socket;
            private int clientNumber;
            ClientInfoPacket clientInfoPacket;
            String fileName;


            public ServerThread(Socket socket, int clientNumber) {
                this.socket = socket;
                this.clientNumber = clientNumber;
                System.out.println("Client-" + clientNumber + "is connected.");

            }

            public void run() {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    while (true) {
                        if (objectInputStream.readObject() instanceof ClientInfoPacket) {
                            clientInfoPacket = (ClientInfoPacket) objectInputStream.readObject();
                           // SQLiteJDBC myDb = new SQLiteJDBC();
                        }
                        if (objectInputStream.readObject() instanceof ClientOutMessage) {

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }

            class SQLiteJDBC
            {
                Connection c=null;
                public SQLiteJDBC()
                {
                    try {
                        Class.forName("org.sqlite.JDBC");
                        c= DriverManager.getConnection("jdbc:sqlite:myDatabase.db");
                    } catch (Exception e) {
                        System.err.println(e.getClass().getName()+":"+e.getMessage());
                        System.exit(0);
                    }
                    System.out.println("Successfully opened database");
                }
            }


