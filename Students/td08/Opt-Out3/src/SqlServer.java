import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class SqlServer
{
    public static boolean createTable (Statement statement) {
        try {
            String sql = "CREATE TABLE DATA (DATE TEXT, IP TEXT, 'MOUSE X' TEXT, 'MOUSE Y' TEXT)";
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
            String sql = "Delete FROM DATA";
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println("Error deleting rows!");
        }
    }

    public static void ConnectToClient (int port) throws IOException {
        //initialize socket variables
        Socket socket = null;
        ServerSocket server = null;

        //create socket and wait for client connection
        try {
            server = new ServerSocket(port);
            socket = server.accept();
            System.out.println("Client Connected!");
        }
        catch (IOException e) {
            System.out.println("Could not establish server socket on port " + port);
            return;
        }


        //initialize input and output streams
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        //initialize connection and statement variables
        Connection connection = null;
        Statement statement = null;
        PreparedStatement pState = null;

        try {
            //import class and connect to existing database/create database
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\SQLite\\testdb.db");
            statement = connection.createStatement();
            System.out.println("Connected to Database!");

            //attempt to create table in database
            System.out.println("Creating table...");
            if (createTable(statement)) {
                //prints if table already exists and attempts to delete rows
                System.out.println("Table already exists! Attempting to flush table...");
                flushTable(statement);
                System.out.println("Table flushed!");
            }
            String sql = "INSERT INTO DATA VALUES (?, ?, ?, ?)";
            pState = connection.prepareStatement(sql);

            //begin data collection
            String sent = "Now Collecting data...";
            output.println(sent);
            while (true) {
                //date output will also be read for exit keyword
                String date = input.readLine();
                if (date.equals("DONE")) {
                    System.out.println("Finished data collection!");
                    break;
                }
                String clientAddress = input.readLine();
                String mouseX = input.readLine();
                String mouseY = input.readLine();

                //using prepared statements to send data to table
                pState.setString(1, date);
                pState.setString(2, clientAddress);
                pState.setString(3, mouseX);
                pState.setString(4, mouseY);
                pState.executeUpdate();
            }
        }
        catch (SQLException s) {
            System.out.println("Error writing to database...");
            s.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                //close all connections, statements, streams, and sockets
                statement.close();
                connection.close();
                input.close();
                output.close();
                socket.close();
                server.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        int keepOpen = 0;
        System.out.println("Current IP Address: " + InetAddress.getLocalHost().getHostAddress());
        while (true) {
            ConnectToClient(9090);
            if ((keepOpen = JOptionPane.showConfirmDialog(null, "Continue with server operations?", "Make Selection", JOptionPane.YES_NO_OPTION)) == 1) {
                break;
            }
        }
        System.exit(0);
    }
} 