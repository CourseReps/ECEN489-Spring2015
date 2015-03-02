import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class FileServer {

    public final static int SOCKET_PORT = 13267;  // you may change this
    public static String FILE_TO_RECEIVE = System.getProperty("user.dir");
    public final static int FILE_SIZE = 6022386; // file size temporary hard coded

    public static void main (String [] args ) throws IOException {
        int current, bytesRead;

        String sql;
        boolean creatingDB;

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        InetAddress IP = InetAddress.getLocalHost();
        System.out.println("IP of my system is: " + IP.getHostAddress());

        System.out.println("Using port number: " + SOCKET_PORT);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ServerSocket servsock = null;
        Socket sock = null;
        PrintWriter out;

        try {
            servsock = new ServerSocket(SOCKET_PORT);
            while (true) {
                System.out.println("Waiting for an android device to connect...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection: " + sock);

                    //Timestamp the file
                    //FILE_TO_RECEIVE += "\\" + System.currentTimeMillis() + sock.getInetAddress().getCanonicalHostName();
                    FILE_TO_RECEIVE += "\\" + System.currentTimeMillis() + "_testing.db";

                    // receive file
                    byte[] mybytearray = new byte[FILE_SIZE];
                    InputStream is = sock.getInputStream();
                    fos = new FileOutputStream(FILE_TO_RECEIVE);
                    bos = new BufferedOutputStream(fos);
                    bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    current = bytesRead;

                    do {
                        bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                        if (bytesRead >= 0) current += bytesRead;
                    } while (bytesRead > -1);

                    bos.write(mybytearray, 0, current);
                    bos.flush();

                    File dbIN = new File(FILE_TO_RECEIVE);
                    out = new PrintWriter(sock.getOutputStream(), true);

                    if(dbIN.exists())
                    {
                        System.out.println("File " + FILE_TO_RECEIVE + " downloaded (" + current + " bytes read)");
                        //out.print("Got file");
                    }
                    else
                    {
                        System.out.println("File " + FILE_TO_RECEIVE + " was not downloaded correctly");
                        //out.print("There was an error downloading the file");
                    }


                }
                finally {
                    if (fos != null) fos.close();
                    if (bos != null) bos.close();
                    if (sock!=null) sock.close();
                }




                //Declare database variables
                Connection dbIN = null, dbOUT = null;
                Statement stmt = null;

                String s = System.getProperty("user.dir");

                //Check if the db file must be created
                File dbfile = new File(s + "\\main.db");
                creatingDB = !dbfile.exists();

                try {
                    //Connect to the database
                    Class.forName("org.sqlite.JDBC");
                    dbIN = DriverManager.getConnection("jdbc:sqlite:" + FILE_TO_RECEIVE);
                    dbIN.setAutoCommit(false);

                    //Class.forName("org.sqlite.JDBC");
                    dbOUT = DriverManager.getConnection("jdbc:sqlite:main.db");
                    dbOUT.setAutoCommit(false);

                    System.out.println("Opened databases successfully");

                    //If the db file was created then create the table CLICKS
                    if (creatingDB) {
                        stmt = dbOUT.createStatement();
                        sql = "CREATE TABLE DATA (ID INTEGER PRIMARY KEY, TIMES LONG, MAC TEXT, PBID LONG)";
                        stmt.executeUpdate(sql);
                    }

                    //Get Points from the client and load them into the database
//            for (int i = 0; i < 10; i++) {
//                stmt = c.createStatement();
//                sql = "INSERT INTO TESTING (X, Y) VALUES (" + i + ", " + i + ");";
//                stmt.executeUpdate(sql);
//
//                //System.out.println("Point (" + p.getX() + ", " + p.getY() + ") received from client and loaded into database");
//            }

                    stmt = dbIN.createStatement();
                    ResultSet rootRS = stmt.executeQuery( "SELECT * FROM ROOT;" );

                    long pbid = rootRS.getLong("PBID");

                    stmt = dbIN.createStatement();
                    ResultSet dataRS = stmt.executeQuery( "SELECT * FROM DATA;" );
                    //long pbid = 1000;
                    long times;
                    String mac;
                    int id = 0;

                    while (dataRS.next()) {
                        id++;
                        times = dataRS.getLong("TIMES");
                        mac = dataRS.getString("MAC");

                        //System.out.println("X: " + x + " Y: " + y);

                        stmt = dbOUT.createStatement();
                        sql = "INSERT INTO DATA (TIMES, MAC, PBID) VALUES (" + times + ", '" + mac + "', " + pbid + ");";
                        stmt.executeUpdate(sql);


                    }

//            stmt = c.createStatement();
//            sql = "INSERT INTO TESTING SELECT * FROM toMerge.TESTING";
//            stmt.executeUpdate(sql);

                    stmt.close();
                    dbIN.commit();
                    dbIN.close();
                    dbOUT.commit();
                    dbOUT.close();
                    System.out.println("Databases closed");
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }

                FILE_TO_RECEIVE = System.getProperty("user.dir");
            }
        }
        finally {
            if (servsock != null) servsock.close();
        }




    }
}
