import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.net.InetAddress;

public class DatabaseServer {

    public static void main(String[] args) throws IOException {
        String sql;
        Point p;
        boolean creatingDB;

        //Create database varibles
        Connection c = null;
        Statement stmt;

        //Create a new server socket
        ServerSocket listener = new ServerSocket(9090);

        //Output the IP address to connect to the server
        InetAddress ipAddress = InetAddress.getLocalHost();
        System.out.println("Current IP Address: " + ipAddress.getHostAddress());

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        //Check if the db file must be created
        File dbfile = new File(s + "\\clicks.db");
        creatingDB = !dbfile.exists();

        try {
            //Connect to the database
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:clicks.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            //If the db file was created then create the table CLICKS
            if (creatingDB) {
                stmt = c.createStatement();
                sql = "CREATE TABLE CLICKS (X DOUBLE, Y DOUBLE)";
                stmt.executeUpdate(sql);
            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        try {
            //Wait for a client to connect
            System.out.println("Waiting for client to connect...");
            Socket socket = listener.accept();
            System.out.println("Client connected from: " + socket.getLocalAddress().getHostName());

            //Create stream for receiving objects
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            try {

                //Get Points from the client and load them into the database
                do {
                    p = (Point)ois.readObject();

                    stmt = c.createStatement();
                    sql = "INSERT INTO CLICKS (X, Y) VALUES (" + p.getX() + ", " + p.getY() + ");";
                    stmt.executeUpdate(sql);

                    System.out.println("Point (" + p.getX() + ", " + p.getY() + ") received from client and loaded into database");

                } while (p.getX() != 0 || p.getY() != 0);

                stmt.close();
                c.commit();
                c.close();
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            finally {
                //Close the stream and the socket
                ois.close();
                socket.close();
                System.out.println("\nSocket was closed");
            }
        }
        finally {
            //Close the server socket
            listener.close();
            System.out.println("ServerSocket was closed");
        }
    }
}