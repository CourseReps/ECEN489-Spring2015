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
               // getting the Port number from user
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the Port number to host the Server");
        int port = in.nextInt();

        System.out.println("Server IP address :" + Inet4Address.getLocalHost());

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("The Server is up!\n \n Waiting for incoming connections...");

        try {

            while (true) {
                new ServerThread(serverSocket.accept()).start();
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
    String clientName;
    SQLiteJDBC myDb;


    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            int count = 0;
            while (true) {
                Object object = objectInputStream.readObject();
                if ( object instanceof ClientInfoPacket) {
                    clientName= ((ClientInfoPacket)object).getClientName();
                    System.out.println("\"" + clientName + "\" is connected.");
                    clientInfoPacket = (ClientInfoPacket) object;
                    myDb = new SQLiteJDBC(clientName);
                }
                if (object instanceof ClientOutMessage) {
                    myDb.insertRow((ClientOutMessage)object);
                }
            }
        }
        catch (IOException e) {
           // e.printStackTrace();
            System.out.println("Inserted records to database\n \n ");

        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
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
            System.out.println("New table created in the name of "+tableName);
        } catch (Exception e) {
            //System.err.println( e.getClass().getName() + ": " + e.getMessage());
            System.out.println("\nWelcome back "+tableName+"! Your new records will be added to the existing table\n \nReceiving data..." );
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


