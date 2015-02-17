        package com.company;

        import com.nagaraj.challenge5android.ClientInfoPacket;
        import com.nagaraj.challenge5android.ClientOutMessage;

        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.net.Inet4Address;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;
        import java.sql.Statement;
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
                SQLiteJDBC myDb;


                public ServerThread(Socket socket, int clientNumber) {
                    this.socket = socket;
                    this.clientNumber = clientNumber;
//                    System.out.println("Client-" + clientNumber + "is connected.");

                }

                public void run() {
                    try {

                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        int count = 0;
                        while (true) {
                            System.out.println("Client-" + clientNumber + "is connected.");
                            Object object = objectInputStream.readObject();
                            if ( object instanceof ClientInfoPacket) {
                                System.out.println("Client-" + clientNumber + "is connected.");
                                clientInfoPacket = (ClientInfoPacket) object;
                                myDb = new SQLiteJDBC(clientInfoPacket.getClientName());
                            }
                            if (object instanceof ClientOutMessage) {
                                myDb.insertRow((ClientOutMessage)object);
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
                    Connection c = null;
                    Statement statement =null;
                    String tableName;
                    public SQLiteJDBC(String tableName)
                    {
                        this.tableName=tableName;
                        try {
                            Class.forName("org.sqlite.JDBC");
                            c= DriverManager.getConnection("jdbc:sqlite:myDatabase.db");
                        } catch (Exception e) {
                            System.err.println(e.getClass().getName()+":"+e.getMessage());
                            System.exit(0);
                        }
                        System.out.println("Successfully opened database");
                        try {
                            statement=c.createStatement();
                            String sql ="CREATE TABLE "+tableName+"(ID INT PRIMARY KEY NOT NULL,"+
                                    "X REAL,"+
                                    "Y REAL,"+
                                    "Z REAL,"+
                                    "DATE_TIME STRING NOT NULL)";
                            statement.executeUpdate(sql);
                        } catch (Exception e) {
                            System.err.println( e.getClass().getName() + ": " + e.getMessage());
                        }

                    }
                    public void insertRow(ClientOutMessage clientOutMessage) {
                        try {
                            statement = c.createStatement();
                            String sql = "INSERT INTO " + tableName + " (ID, X, Y, Z, DATE_TIME) " +
                                    "VALUES ("+clientOutMessage.id+", "+clientOutMessage.X+", "+clientOutMessage.Y+", "+clientOutMessage.Z+", '"+clientOutMessage.date+"' );";
                            statement.executeUpdate(sql);
                        } catch (SQLException e) {
                            System.err.println(e.getClass().getName() + ": " + e.getMessage());

                        }
                    }

                    }


