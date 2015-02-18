import java.sql.*;

public class DBHandler implements Runnable {

    ServerRunnable parent;
    Connection c;
    Statement stmt;
    String sqlCommand;

    DBHandler(ServerRunnable parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        c = null;
        stmt = null;
        sqlCommand = null;

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:server.db");

            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "ROOT", null);

            if (tables.next()) {
                parent.newMessage("Table exists");

            } else {
                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE ROOT " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " CLIENTID           LONG    NOT NULL, " +
                        " NUMTABLES            INT     NOT NULL, " +
                        " TABLE1        CHAR(50));";
                stmt.executeUpdate(sqlCommand);

                parent.newMessage("Created ID table successfully");
            }
        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
        }
        parent.newMessage("Opened database successfully");
    }


    public long getLatestUpdateTime(long clientID) {
        ResultSet rs = null;
        long returnValue = 0;

        try {

            stmt = c.createStatement();
            sqlCommand = "SELECT * FROM C" + String.valueOf(clientID) + " ORDER BY ID DESC LIMIT 1";
            rs = stmt.executeQuery(sqlCommand);
            returnValue = Long.valueOf(rs.getLong("TIME"));

        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return 0;

        } finally {
            parent.newMessage("Latest entry timestamp: " + String.valueOf(returnValue));
            return returnValue;
        }
    }


    public ResultSet dbOperation(String sqlCommand) {
        ResultSet rs = null;
        long returnValue = 0;

        try {

            stmt = c.createStatement();
            rs = stmt.executeQuery(sqlCommand);

        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return null;

        } finally {
            return rs;
        }
    }

    public boolean clientDBexists(long clientID) {

        try {

            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "C" + String.valueOf(clientID), null);

            if (tables.next()) {
                parent.newMessage("Table exists");
                return true;

            } else {
                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE C" + String.valueOf(clientID) + " " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " TIME           LONG    NOT NULL, " +
                        " NAME            CHAR(50)     NOT NULL, " +
                        " DATA        CHAR(50)      NOT NULL);";
                stmt.executeUpdate(sqlCommand);

                parent.newMessage("Created table for client " + String.valueOf(clientID));
            }
        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        parent.newMessage("Opened database successfully");
        return true;
    }

    public boolean clientEntry(String key, String clientID, String time, String data) {

        try {
            stmt = c.createStatement();
            sqlCommand = "INSERT INTO C" + clientID + " (ID, TIME, NAME, DATA) " +
                    "VALUES ( " + key + ", " + clientID + ", " + time + ", " + data + " );";
            stmt.executeUpdate(sqlCommand);

        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean clientEntry(long clientID, String line) {

        try {
            stmt = c.createStatement();
            sqlCommand = "INSERT INTO C" + String.valueOf(clientID) + " (ID, TIME, NAME, DATA) " +
                    "VALUES ( " + line + " );";
            stmt.executeUpdate(sqlCommand);

        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }
}