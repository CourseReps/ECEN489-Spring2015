import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;

public class DBHandler implements Runnable {

    Connection c;
    Statement stmt;
    String sqlCommand;

    DBHandler() { }

    @Override
    public void run() {
        c = null;
        stmt = null;
        sqlCommand = null;

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:prombox.db");

            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "ROOT", null);

            if (tables.next()) {
                System.out.print("Table exists");

            } else {
                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE ROOT " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " PBID           INT    NOT NULL);";
                stmt.executeUpdate(sqlCommand);

                int PBID = 2;
                sqlCommand = "INSERT INTO ROOT (ID, PBID) VALUES ( 1, " + PBID + " );";
                stmt.executeUpdate(sqlCommand);

                System.out.print("Created ID table successfully");
            }
        } catch (Exception e) {
            System.out.print(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.print("Opened database successfully");

        checkDataDB();
    }

    public boolean checkDataDB() {

        try {

            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "DATA", null);

            if (tables.next()) {
                System.out.print("DATA Table exists");
                return true;

            } else {
                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE DATA " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " TIMES           LONG    NOT NULL, " +
                        " MAC            CHAR(50)     NOT NULL)";
                stmt.executeUpdate(sqlCommand);

                System.out.print("Created DATA table");
            }
        } catch (Exception e) {
            System.out.print(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        System.out.print("Opened DATA database successfully");
        return true;
    }

    public boolean dataEntry(String time, HashSet<String> entrySet) {

        try {
            stmt = c.createStatement();

            Iterator<String> iterator = entrySet.iterator();
            while (iterator.hasNext()) {

                String thisMac = iterator.next();
                sqlCommand = "INSERT INTO DATA (TIMES, MAC) " +
                        "VALUES ( " + time + ", '" + thisMac + "' )";

                stmt.addBatch(sqlCommand);
            }
            stmt.executeBatch();

        } catch (Exception e) {
            System.out.print(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }
}