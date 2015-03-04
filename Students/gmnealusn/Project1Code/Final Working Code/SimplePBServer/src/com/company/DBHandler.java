package com.company;

import java.io.*;
import java.sql.*;

/**
 * Created by tungala on 2/27/2015.
 */
public class DBHandler {

    private Integer lastPbId;
    private Integer lastSvrId;
    private Connection c;
    private Connection localC;
    private Statement stmt;
    private String sqlCommand;
    private Integer lastTxId;
    private String r2Name;

    public DBHandler(String lastPbTime, String lastSvrTime,String r2Name) {

        this.lastPbId = Integer.parseInt(lastPbTime);
        this.lastSvrId = Integer.parseInt(lastSvrTime);
        this.r2Name = r2Name;

    }

    public void createTransferDB() {
        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:PB4.db");
            createNewDB(".//PB4.db",".//"+r2Name+"localprombox.db");
            localC = DriverManager.getConnection("jdbc:sqlite:"+r2Name+"localprombox.db");

            checkRoot();
            checkDb();
           // checkLocalRoot();
           // checkLocalDb();
            populateLocalDB();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkRoot()throws Exception{

        DatabaseMetaData dbm = c.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "ROOT", null);

        if (tables.next()) {
            System.out.println("Table exists");

        } else {
            stmt = c.createStatement();
            sqlCommand = "CREATE TABLE ROOT " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " PBID           TEXT   NOT NULL);";
            stmt.executeUpdate(sqlCommand);

            //long PBID = Math.abs(new HighQualityRandom().nextLong());
            sqlCommand = "INSERT INTO ROOT (ID, PBID) VALUES ( 1, '2' );";
            stmt.executeUpdate(sqlCommand);

            System.out.println("Created ID table successfully");
            stmt.close();
        }

    }

    private void checkDb() throws Exception{
        DatabaseMetaData dbm = c.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "DATA", null);

        if (tables.next()) {
            System.out.println("DATA Table exists");

        } else {
            stmt = c.createStatement();
            sqlCommand = "CREATE TABLE DATA " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " TIMES           LONG    NOT NULL, " +
                    " MAC            CHAR(50)     NOT NULL)";
            stmt.executeUpdate(sqlCommand);

            System.out.println("Created DATA table");
            stmt.close();
        }


        System.out.println("Opened DATA database successfully");
    }

    private void checkLocalRoot()throws Exception{

        DatabaseMetaData dbm = localC.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "ROOT", null);

        if (tables.next()) {
            System.out.println("Table exists");

        } else {
            stmt = localC.createStatement();
            sqlCommand = "CREATE TABLE ROOT " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " PBID           TEXT   NOT NULL);";
            stmt.executeUpdate(sqlCommand);

            //long PBID = Math.abs(new HighQualityRandom().nextLong());
            sqlCommand = "INSERT INTO ROOT (ID, PBID) VALUES ( 1, 'PB4' );";
            stmt.executeUpdate(sqlCommand);

            System.out.println("Created ID table successfully");
            stmt.close();
        }

    }

    private void checkLocalDb() throws Exception{
        DatabaseMetaData dbm = localC.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "DATA", null);

        if (tables.next()) {
            System.out.println("DATA Table exists");

        } else {
            stmt = localC.createStatement();
            sqlCommand = "CREATE TABLE DATA " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " TIMES           LONG    NOT NULL, " +
                    " MAC            CHAR(50)     NOT NULL)";
            stmt.executeUpdate(sqlCommand);

            System.out.println("Created DATA table");
            stmt.close();
        }


        System.out.println("Opened Local database successfully");
    }

    private void populateLocalDB()throws Exception{




        try {
            if(lastSvrId!=0) {
                sqlCommand = "delete from DATA where ID < "+lastSvrId+";";
                stmt = localC.createStatement();
                stmt.executeUpdate(sqlCommand);
                System.out.println("Successfully populated local DataBase");
                stmt.execute("VACUUM DATA;");

            }
            sqlCommand = "select * from DATA;";
            stmt = localC.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);
            while(rs.next()){
                setLastTxId(rs.getInt(1));
            }

            rs.close();
            stmt.close();
            c.close();
            localC.close();

        }
        catch(Exception e) {
            System.out.println("data manipulation failed");
            e.printStackTrace();
        }



    }



    private void createNewDB(String mainDb, String newDb){
        try {
            File oldDb =new File(mainDb);
            File neoDb =new File(newDb);

            InputStream inStream = new FileInputStream(oldDb);
            OutputStream outStream = new FileOutputStream(neoDb);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0){

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            System.out.println("File is copied successful!");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getLastTxId() {
        return lastTxId;
    }

    private void setLastTxId(Integer lastTxId) {
        this.lastTxId = lastTxId;
    }
}
