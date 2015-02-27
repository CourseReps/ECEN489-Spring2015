package com.company;

import java.sql.*;

/**
 * Created by tungala on 2/27/2015.
 */
public class DBHandler {

    private Long lastPbTime;
    private Long lastSvrTime;
    private Connection c;
    private Connection localC;
    private Statement stmt;
    private String sqlCommand;
    private Long lastTxTime;

    public DBHandler(String lastPbTime, String lastSvrTime) {

        this.lastPbTime = Long.parseLong(lastPbTime);
        this.lastSvrTime = Long.parseLong(lastSvrTime);

    }

    public void createTransferDB() {
        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\RhoadsWylde\\Desktop\\Master Clone\\prombox.db");

            localC = DriverManager.getConnection("jdbc:sqlite:prombox.db");

            checkRoot();
            checkDb();
            checkLocalRoot();
            checkLocalDb();
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
            sqlCommand = "INSERT INTO ROOT (ID, PBID) VALUES ( 1, 'PB2' );";
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
            sqlCommand = "INSERT INTO ROOT (ID, PBID) VALUES ( 1, 'PB2' );";
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


        System.out.println("Opened DATA database successfully");
    }

    private void populateLocalDB()throws Exception{


        if(lastPbTime==0 || lastSvrTime==0) {
            sqlCommand = "select * from DATA";
        }
        else {
            sqlCommand = "select * from DATA where TIMES>"+lastSvrTime;
        }
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(sqlCommand);

        Statement stmt2 = localC.createStatement();
        while(rs.next()){
            String localQuery = "insert into DATA values("+rs.getLong(1)+",'"+rs.getString(2)+"');";
            stmt2.executeUpdate(localQuery);
            setLastTxTime(rs.getLong(1));
        }

        rs.close();
        stmt.close();
        stmt2.close();

        System.out.println("Successfully created local DataBase");

    }

    public Long getLastTxTime() {
        return lastTxTime;
    }

    private void setLastTxTime(Long lastTxTime) {
        this.lastTxTime = lastTxTime;
    }
}
