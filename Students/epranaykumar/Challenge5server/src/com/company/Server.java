package com.company;



import java.io.*; // ioexception and objectoutputstream
import java.net.*;
import java.lang.Integer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import java.sql.*;

/**
 * A server program which accepts requests from clients to
 * capitalize strings.  When clients connect, a new thread is
 * started to handle an interactive dialog in which the client
 * sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform
 * dependent.  If you ran it from a console window with the "java"
 * interpreter, Ctrl+C generally will shut it down.
 */
public class Server {



    public static boolean createTable (Statement statement, String ctnumber) {
        try {
            //String sql = "CREATE TABLE DATA (DATE TEXT, IP TEXT, 'MOUSE X' TEXT, 'MOUSE Y' TEXT)";
            String sql = "CREATE TABLE "+"t_"+ ctnumber + " (ID INT, X FLOAT, Y FLOAT, Z FLOAT)";
            //String sql = "CREATE TABLE SENSOR_DATA(ID INT, X FLOAT, Y FLOAT, Z FLOAT)";
            statement.executeUpdate(sql);
            System.out.println("Created new table!");
            return false;
        }
        catch (SQLException e) {
            return true;
        }
    }

    public static void flushTable (Statement statement) {
        try {
            String sql = "Delete FROM SENSOR_DATA";
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println("Error deleting rows!");
        }
    }


    /**
     * Application method to run the server runs in an infinite loop
     * listening on port 9898.  When a connection is requested, it
     * spawns a new thread to do the servicing and immediately returns
     * to listening.  The server keeps a unique client number for each
     * client that connects just to show interesting logging
     * messages.  It is certainly not necessary to do this.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The capitalization server is running.");
        System.out.println("Server IP address:" + InetAddress.getLocalHost().getHostAddress() + "\nServer PORT number:9898");
        int clientNumber = 1;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new ClientConnector(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A private thread to handle capitalization requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class ClientConnector extends Thread {
        private Socket socket;
        private int clientNumber;

        public ClientConnector(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                 BufferedReader serverInput = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                //ObjectInputStream serverInput =new ObjectInputStream(socket.getInputStream());

                // Send a welcome message to the client.
                // out.println("Hello, you are client #" + clientNumber + ".");


                Connection connection = null;
                Statement statement = null;
                PreparedStatement pState = null;


                //import class and connect to existing database/create database
                Class.forName("org.sqlite.JDBC");
                //connection = DriverManager.getConnection("jdbc:sqlite:C:\\SQLite\\testdb.db");
                connection = DriverManager.getConnection("jdbc:sqlite:P:\\Masters Courses\\Mobile Sensing\\sqlite3\\SensorReadings.db");
                statement = connection.createStatement();
                System.out.println("Connected to Database!");

                String ctnumber = Integer.toString(clientNumber);
                //String sql = "CREATE TABLE "+ "t_"+ctnumber + " (ID INT, X FLOAT, Y FLOAT, Z FLOAT)";
                //statement.executeUpdate(sql);

                //attempt to create table in database
                System.out.println("Creating table...");


               if (createTable(statement,ctnumber)) {
                    //prints if table already exists and attempts to delete rows
                    System.out.println("Table already exists! Attempting to flush table...");
                    flushTable(statement);
                    System.out.println("Table flushed!");
                }
                String sql1 = "INSERT INTO "+ "t_"+ ctnumber + " VALUES (?, ?, ?, ?)";
                pState = connection.prepareStatement(sql1);

                //begin data collection
                String sent = "Now Collecting data...";
                System.out.println(sent);
                String loopFactor;
                while(true) {

                    if((loopFactor = serverInput.readLine())!= null) {
                        if (loopFactor.equals("PAUSE")) {

                        } else if (loopFactor.equals("RUN")) {

                            //int ID = Integer.parseInt (serverInput.readLine());
                            // float X = Float.parseFloat(serverInput.readLine());
                            // float Y = Float.parseFloat(serverInput.readLine());
                            // float Z = Float.parseFloat(serverInput.readLine());
                            String ID = (serverInput.readLine());
                            String X = (serverInput.readLine());
                            String Y = (serverInput.readLine());
                            String Z = (serverInput.readLine());


                            System.out.printf(" id:%s     X:%s     Y:%s     Z:%s \n", ID, X, Y, Z);
                            //using prepared statements to send data to table
                            pState.setString(1, ID);
                            pState.setString(2, X);
                            pState.setString(3, Y);
                            pState.setString(4, Z);
                            pState.executeUpdate();
                        } else if (loopFactor.equals("STOP")) {
                            break;
                        }
                    }
                }


            }
            catch (SQLException s) {
                System.out.println("Error writing to database...");
                s.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
                log("Error handling client# " + clientNumber + ": " + e);
            }
            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, ");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
    }
}