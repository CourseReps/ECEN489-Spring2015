import java.sql.*;

public class DBManager implements Runnable {
    //initialize connection and statement variables
    Connection connection = null;
    Statement statement = null;
    PreparedStatement pState = null;
    final String DB_NAME = "testdb.db";
    final String TABLE_NAME = "DATA";

    public void run() {
        try {
            //import class and connect to existing database/create database
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\SQLite\\" + DB_NAME);
            statement = connection.createStatement();
            System.out.println("Connected to Database!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createTable() {
        System.out.println("Attempting to create table...");
        boolean tableExists = false;
        try {
            String sql = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, LAT DOUBLE, LONG DOUBLE)";
            statement.executeUpdate(sql);
            System.out.println("Successfully created new table: " + TABLE_NAME);
        }
        catch (SQLException s) {
            System.out.println("Table already exists!");
            tableExists = true;
        }
        finally {
            //if table exists, return true value
            return tableExists;
        }
    }

    public boolean tableExists() {

        boolean tableExists = false;
        try {
            DatabaseMetaData dbMeta = connection.getMetaData();
            //TODO: update table name if necessary
            ResultSet tableCheck = dbMeta.getTables(null, null, TABLE_NAME, null);
            if (tableCheck.next())
                tableExists = true;
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        finally {
            return tableExists;
        }
    }

    public void insertData (String data) {
        try {
            statement.executeUpdate(data);
            System.out.println("Successfully wrote entry: " + data + " to " + TABLE_NAME);
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
