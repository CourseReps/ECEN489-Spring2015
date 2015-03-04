package com.company;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class FileServer extends Thread{

    public void run() {

    final int SOCKET_PORT = 13267;  // you may change this
    String FILE_TO_RECEIVE = System.getProperty("user.dir");
    final int FILE_SIZE = 6022386; // file size temporary hard coded

    //public static void main (String [] args ) throws IOException {
        int current, bytesRead;
        int numFiles, numBytes, lastID;

        Connection dbIN = null, dbOUT, dbFinal;
        Statement stmt;

        String sql;
        boolean creatingDB;

        InetAddress IP = null;
        try {
            IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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

                    BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));


                    numFiles = Integer.parseInt(reader.readLine());

                    for (int i = 0; i < numFiles; i++)
                    {
                        //Timestamp the file
                        //FILE_TO_RECEIVE += "\\" + System.currentTimeMillis() + sock.getInetAddress().getCanonicalHostName();
                        FILE_TO_RECEIVE += "\\" + System.currentTimeMillis() + "_testing.db";

                        numBytes = Integer.parseInt(reader.readLine());

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

                        //File dbIN = new File(FILE_TO_RECEIVE);
                        out = new PrintWriter(sock.getOutputStream(), true);

                        if(numBytes == bytesRead)
                        {
                            System.out.println("File " + FILE_TO_RECEIVE + " downloaded (" + current + " bytes read)");

                            Class.forName("org.sqlite.JDBC");
                            dbIN = DriverManager.getConnection("jdbc:sqlite:" + FILE_TO_RECEIVE);
                            dbIN.setAutoCommit(false);
                            //out.print("Got file");

                            stmt = dbIN.createStatement();
                            ResultSet rs = stmt.executeQuery( "SELECT * FROM DATA ORDER BY TIME DESC LIMIT 1;");

                            lastID = rs.getInt("ID");

                            out.println(String.valueOf(lastID));
                        }
                        else
                        {
                            System.out.println("File " + FILE_TO_RECEIVE + " was not downloaded correctly");
                            //out.print("There was an error downloading the file");
                        }
                    }
                }
                catch ( Exception e1 ) {
                    System.err.println( e1.getClass().getName() + ": " + e1.getMessage() );
                    System.exit(0);
                }
                finally {
                    if (fos != null) try {
                        fos.close();
                        if (bos != null) bos.close();
                        if (sock!=null) sock.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }






                //Declare database variables
                //Connection dbIN, dbOUT, dbFinal;
                //Statement stmt;
                String newDB = "test2.db";
                String finalDB = "final.db";
                int numFolders;
                PrintWriter toClient;


                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                String s = System.getProperty("user.dir");

                //Check if the db file must be created
                File dbfile = new File(s + "\\" + newDB);
                creatingDB = !dbfile.exists();

                try {
                    //Connect to the database
                   /* Class.forName("org.sqlite.JDBC");
                    dbIN = DriverManager.getConnection("jdbc:sqlite:" + FILE_TO_RECEIVE);
                    dbIN.setAutoCommit(false);*/

                    dbOUT = DriverManager.getConnection("jdbc:sqlite:" + newDB);
                    dbOUT.setAutoCommit(false);

                    System.out.println("Opened databases successfully");

                    //If the db file was created then create the table CLICKS
                    if (creatingDB) {
                        stmt = dbOUT.createStatement();
                        sql = "CREATE TABLE DATA (PBID INTEGER, TIME LONG, MAC TEXT)";
                        stmt.executeUpdate(sql);
                    }

                    dbFinal = DriverManager.getConnection("jdbc:sqlite:" + finalDB);
                    dbFinal.setAutoCommit(false);

                    stmt = dbIN.createStatement();
                    ResultSet rootRS = stmt.executeQuery( "SELECT * FROM PBID;" );

                    long pbid = rootRS.getLong("ID");

                    //Delete any duplicate data
                    stmt = dbIN.createStatement();
                    sql = "DELETE FROM DATA WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM DATA GROUP BY TIME, MAC);";
                    stmt.executeUpdate(sql);

                    //Get the first time
                    stmt = dbIN.createStatement();
                    ResultSet dataRS = stmt.executeQuery( "SELECT * FROM DATA;" );

                    long time;
                    String mac;

                    while (dataRS.next()) {
                        time = dataRS.getLong("TIME");
                        mac = dataRS.getString("MAC");

                        stmt = dbOUT.createStatement();
                        sql = "INSERT INTO DATA (PBID, TIME, MAC) VALUES (" + pbid + ", " + time + ", '" + mac + "');";
                        stmt.executeUpdate(sql);
                    }

                    //Check if the db file must be created
                    File dbFinalfile = new File(s + "\\" + finalDB);
                    creatingDB = !dbFinalfile.exists();

                    //If the db file was created then create the table CLICKS
                    //if (creatingDB) {
                    stmt = dbFinal.createStatement();
                    //sql = "CREATE TABLE DATA (ID INTEGER PRIMARY KEY, TIMES LONG, MAC TEXT, PBID LONG)";
                    sql = "CREATE TABLE DATA (TIME TEXT, NUM_MACS INTEGER, NUM_PEOPLE INTEGER, PBID INTEGER, ADDED TEXT)";
                    stmt.executeUpdate(sql);
                    // }


                    stmt = dbOUT.createStatement();
                    ResultSet timeRS = stmt.executeQuery( "SELECT * FROM DATA WHERE ROWID = 1;");
                    long startTime = timeRS.getLong("TIME");

                    //stmt = dbOUT.createStatement();
                    timeRS = stmt.executeQuery( "SELECT * FROM DATA ORDER BY TIME DESC LIMIT 1;");
                    long endTime = timeRS.getLong("TIME");

                    ResultSet finalRS;
                    long end;
                    int macCtr;

                    for(long i = startTime; i <= endTime; i += 10)
                    {
                        end = i + 10;
                        stmt = dbOUT.createStatement();
                        finalRS = stmt.executeQuery("SELECT * FROM DATA WHERE (TIME >= " + i + " AND TIME < " + end + ") GROUP BY MAC;");
                        //stmt.executeUpdate(sql);

                        if(!finalRS.isBeforeFirst())
                            macCtr = 1;
                        else
                            macCtr = 0;

                        while(finalRS.next())
                            macCtr++;

                        stmt = dbFinal.createStatement();
                        sql = "INSERT INTO DATA (TIME, NUM_MACS, NUM_PEOPLE, PBID, ADDED) VALUES ('" + DATE_FORMAT.format(i * 1000) + "', " + macCtr + ", 0, " + pbid + ", 'NO');";
                        stmt.executeUpdate(sql);
                    }

                    //dbfile.delete();


                    stmt.close();
                    dbIN.commit();
                    dbIN.close();
                    dbOUT.commit();
                    dbOUT.close();
                    dbFinal.commit();
                    dbFinal.close();
                    System.out.println("Databases closed");
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }

                FILE_TO_RECEIVE = System.getProperty("user.dir");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servsock != null) try {
                servsock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }
}
