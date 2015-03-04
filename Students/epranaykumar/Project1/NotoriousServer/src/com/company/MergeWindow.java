package com.company;

/**
 * Created by PRANAY KUMAR on 3/1/2015.
 */

import java.io.File;
import java.net.Socket;
import java.sql.*;


public class MergeWindow implements Runnable {

    static boolean transferConfirmation;
    static long lastTimeStamp, lastTSBeforeMerge, receivedFirstTS, receivedFirstID, lastTSwindowed = 0, lastTSAfterMerge, firstTS;
    static String lastMACAfterMerge;
    static int lastIDAfterMerge;
    Integer receivedID;

    private Socket socket;

    MergeWindow (Socket socket){
        this.socket = socket;
    }



    public void run(){


        transferConfirmation = false;
        //Declare database variables
        Connection mainConnect, receivedConnect,regressionConnect;
        Statement mainstmt = null;
        Statement receivedstmt = null;
        Statement regressionstmt = null;


        String addDB = MuleData.dbReceived;

        // String s = System.getProperty("user.dir");

        //Check if the db file must be created
            /*File dbfile = new File(s + "\\main.db");
            creatingDB = !dbfile.exists();*/

        try {
            //Connect to the database
            Class.forName("org.sqlite.JDBC");
            mainConnect = DriverManager.getConnection("jdbc:sqlite:P:\\Masters Courses\\Mobile Sensing\\sqlite3\\master.db "); //connects to main database.
            mainConnect.setAutoCommit(false);// start transaction

            final String lin_Reg_DB_NAME = "regression.db";
            regressionConnect = DriverManager.getConnection("jdbc:sqlite:P:\\Masters Courses\\Mobile Sensing\\sqlite3\\" + lin_Reg_DB_NAME); //connects to main database.
            regressionConnect.setAutoCommit(false);

            receivedConnect = DriverManager.getConnection("jdbc:sqlite:" + addDB); //connects to received database
            receivedConnect.setAutoCommit(false);

            System.out.println("Opened database successfully");

            //Creates the table if the main database is being initially created.

            try {
                mainstmt = mainConnect.createStatement();
                // stmt = mainConnect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                //sql = "CREATE TABLE DATA (ID INT,TIMES LONG, MAC TEXT, PRIMARY KEY (TIMES, MAC) ON CONFLICT REPLACE )";
                String sql = "CREATE TABLE DATA (ID INT,TIME LONG, MAC TEXT, PBID INT )";
                mainstmt.executeUpdate(sql);
                System.out.println("DATA table created in master db");

            }
            catch(Exception e)
            {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
            }

            regressionstmt = regressionConnect.createStatement();

            try {
                String sql11 = "CREATE TABLE DATA2 (TIME LONG, NUM_MACS INT, NUM_PEOPLE INT, PBID INT, ADDED TEXT)";
                regressionstmt.executeUpdate(sql11);
                System.out.println("DATA table created in regression db");
                //regressionstmt.executeUpdate("INSERT INTO DATA VALUES(0,0,0,0,'NO')");

            }
            catch(Exception e)
            {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
            }



            mainstmt = mainConnect.createStatement();

            try{

                receivedstmt = receivedConnect.createStatement();
                ResultSet receivedIDrs = receivedstmt.executeQuery( "SELECT * FROM PBID;" );//checks result set in table DATA from received database
                receivedIDrs.next();
                receivedID = receivedIDrs.getInt("ID");



                ResultSet receivedDATArs1 = receivedstmt.executeQuery( "SELECT * FROM DATA" );
                receivedDATArs1.next();
                receivedFirstID = receivedDATArs1.getInt("ID");
                receivedFirstTS = receivedDATArs1.getLong("TIME");


                receivedstmt.executeUpdate("ALTER TABLE DATA ADD COLUMN PBID INT");
                receivedstmt.executeUpdate("UPDATE DATA SET PBID = "+String.valueOf(receivedID)+" WHERE ID >="+String.valueOf(receivedFirstID));



                System.out.println("transaction started with received id :" + String.valueOf(receivedID));

                ResultSet mainrs1 = mainstmt.executeQuery("SELECT * FROM DATA WHERE PBID ="+String.valueOf(receivedID)+";");
                while (mainrs1.next()) {         //loops and moves cursor to next row until cursor hits the last row.
                    lastTSBeforeMerge = mainrs1.getLong("TIME");
                }



                if (receivedFirstTS < lastTSBeforeMerge) {

                    System.out.println("received TS less than Last TS !");

                    receivedstmt.executeUpdate("DELETE FROM DATA WHERE TIME < "+String.valueOf(lastTSBeforeMerge)+";");

                    ResultSet rs1 = receivedstmt.executeQuery( "SELECT * FROM DATA WHERE TIME =  "+ String.valueOf(lastTSBeforeMerge)+";" );

                    while (rs1.next()){

                        System.out.println("entered while loop ");

                        String tempMac = rs1.getString("MAC");
                        ResultSet ms1 = mainstmt.executeQuery( "SELECT * FROM DATA WHERE PBID = "+String.valueOf(receivedID)+" AND TIME = "+String.valueOf(lastTSBeforeMerge)+" AND MAC LIKE '"+tempMac+"';" );

                        if (ms1.next()) {

                            Integer tempID = rs1.getInt("ID");
                            receivedstmt.executeUpdate("DELETE FROM DATA WHERE ID = "+Integer.toString(tempID)+";");
                            System.out.println("completed delete in if loop ");
                        }
                        // required as the resultset rs1 no longer exists after deleting rows in the table it was connected to i.e., DATA

                        rs1 = receivedstmt.executeQuery("SELECT * FROM DATA WHERE TIME =  " + String.valueOf(lastTSBeforeMerge) + ";");


                    }

                }

                // to get the changes we made to received database, we need to end the transaction. Only then we can access the modified database


                //receivedstmt.executeUpdate("end transaction");
                receivedstmt.close();
                receivedConnect.commit();
                receivedConnect.close();


                // to use attach, we need to end the transaction of the main statement and then begin the transaction
                mainstmt.executeUpdate("end transaction");

                String sql124 = "ATTACH '"+ addDB + "' AS SecondaryDB";
                mainstmt.executeUpdate(sql124);

                mainstmt.executeUpdate("begin transaction");

                System.out.println("inserting data ");

                String sql23= "INSERT INTO  DATA SELECT * FROM SecondaryDB.DATA" ;
                mainstmt.executeUpdate(sql23);

                System.out.println("inserted data ");

            }

            catch (SQLException s) {
                System.out.println("Below SQLException has occurred");

                System.err.println(s.getClass().getName() + ": " + s.getMessage() );
            }


            mainstmt.close();
            mainConnect.commit();
            mainConnect.close();

            regressionstmt.close();
            regressionConnect.commit();
            regressionConnect.close();

// TODO regression table

            mainConnect = DriverManager.getConnection("jdbc:sqlite:P:\\Masters Courses\\Mobile Sensing\\sqlite3\\master.db "); //connects to main database.
            mainConnect.setAutoCommit(false);// start transaction
            mainstmt = mainConnect.createStatement();

            regressionConnect = DriverManager.getConnection("jdbc:sqlite:P:\\Masters Courses\\Mobile Sensing\\sqlite3\\" + lin_Reg_DB_NAME); //connects to main database.
            regressionConnect.setAutoCommit(false);
            // without below statement it doesn't recognise ""regressionstmt"" even though declared in above try block
            regressionstmt = regressionConnect.createStatement();

            System.out.println("regression table ");

            ResultSet rs5 = mainstmt.executeQuery("SELECT * FROM DATA WHERE PBID ="+String.valueOf(receivedID)+";");
            if(rs5.next()){
                firstTS= rs5.getLong("TIME");
                while (rs5.next()){

                    lastTSAfterMerge = rs5.getLong("TIME");
                    lastMACAfterMerge = rs5.getString("MAC");
                    lastIDAfterMerge = rs5.getInt("ID");
                }

            }




            lastTSwindowed= firstTS;
            try {

                ResultSet regrs = regressionstmt.executeQuery("SELECT * FROM DATA2 WHERE PBID =" + String.valueOf(receivedID) + ";");
                while (regrs.next()) {         // to avoid pBId with null data
                    lastTSwindowed = regrs.getLong("TIME");
                }

                System.out.println("lastTSwindowed:"+String.valueOf(lastTSwindowed));
            }
            catch (NullPointerException ne){
                System.out.println("there were no entries for received id:"+ String.valueOf(receivedID)+" in regression table");
            }




            if  (((lastTSAfterMerge-lastTSwindowed)>20)) {



                long tempts = lastTSwindowed;

                System.out.println(" lastTSAfterMerge = "+ String.valueOf(lastTSAfterMerge));
                System.out.println("lastTSAfterMerge-lastTSwindowed) is greater than 20");

                while(( tempts < (lastTSAfterMerge) )){ // < lastTSaftermerge because there can be more entries at lastTSAfterMerge. SO dont include  lastTSAfterMerge in windowing

                    //System.out.println("before rs3 and tempts = "+ String.valueOf(tempts));

                    //try {
                    long maxtempts = tempts + 10;
                    long mintempts = tempts + 1;
                    String sql145= "SELECT DISTINCT MAC FROM DATA WHERE PBID =" + String.valueOf(receivedID) + " AND TIME IN (" + String.valueOf(mintempts) + "," + String.valueOf(maxtempts) + ");";
                    //  ResultSet mainrs3 = mainstmt.executeQuery("SELECT DISTINCT MAC FROM DATA WHERE PBID =" + String.valueOf(receivedID) + " AND TIME IN (" + String.valueOf(mintempts) + "," + String.valueOf(maxtempts) + ");");
                    ResultSet mainrs3 = mainstmt.executeQuery(sql145);
                    //System.out.println(sql145);
                    int count=0;
                    tempts = maxtempts ;
                    while(mainrs3.next()){
                        count= count + 1;
                    }
                    // System.out.println("before updte ");
                    regressionstmt.executeUpdate("INSERT INTO DATA2 VALUES(" + String.valueOf(tempts) + ", " + String.valueOf(count) + ", null, " + String.valueOf(receivedID) + ", 'NO');");

                    // }
                    //catch (NullPointerException ne){
                    //    System.out.println("mainrs3 is null");
                    //}
                }

            }



            System.out.println("last time stamp "+ String.valueOf(lastTSAfterMerge));
            System.out.println("last mac address "+ lastMACAfterMerge);
            transferConfirmation = true;
            transferConfirmation = false;

            mainstmt.close();
            mainConnect.commit();
            mainConnect.close();
            regressionstmt.close();
            regressionConnect.commit();
            regressionConnect.close();
            System.out.println("Database closed");
        }

        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }



    }



}

