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
            c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\RhoadsWylde\\Desktop\\Master Clone\\prombox.db");

            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "ROOT", null);

            if (tables.next()) {
                parent.newMessage("Table exists");

            } else {
                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE ROOT " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " PBID           LONG    NOT NULL);";
                stmt.executeUpdate(sqlCommand);

                long PBID = Math.abs(new HighQualityRandom().nextLong());
                sqlCommand = "INSERT INTO ROOT (ID, PBID) VALUES ( 1, " + PBID + " );";
                stmt.executeUpdate(sqlCommand);

                parent.newMessage("Created ID table successfully");
            }
        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
        }
        parent.newMessage("Opened database successfully");

        checkDataDB();
    }


    public long getLatestUpdate() {
        ResultSet rs = null;
        long returnValue = 0;

        try {

            stmt = c.createStatement();
            sqlCommand = "SELECT * FROM DATA ORDER BY ID DESC LIMIT 1";
            rs = stmt.executeQuery(sqlCommand);
            returnValue = Long.valueOf(rs.getLong("ID"));

        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return 0;

        } finally {
            parent.newMessage("Latest entry ID: " + String.valueOf(returnValue));
            return returnValue;
        }
    }

    public long getMyID() {

        ResultSet rs = null;
        long returnValue = 0;

        try {

            stmt = c.createStatement();
            sqlCommand = "SELECT * FROM ROOT WHERE ID=1";
            rs = stmt.executeQuery(sqlCommand);
            returnValue = Long.valueOf(rs.getLong("PBID"));

        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return 0;

        } finally {
            parent.newMessage("Latest entry ID: " + String.valueOf(returnValue));
            return returnValue;
        }
    }


//    public ResultSet dbOperation(String sqlCommand) {
//        ResultSet rs = null;
//        long returnValue = 0;
//
//        try {
//
//            stmt = c.createStatement();
//            rs = stmt.executeQuery(sqlCommand);
//
//        } catch (Exception e) {
//            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
//            return null;
//
//        } finally {
//            return rs;
//        }
//    }

    public boolean checkDataDB() {

        try {

            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "DATA", null);

            if (tables.next()) {
                parent.newMessage("DATA Table exists");
                return true;

            } else {
                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE DATA " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " TIMES           LONG    NOT NULL, " +
                        " MAC            CHAR(50)     NOT NULL)";
                stmt.executeUpdate(sqlCommand);

                parent.newMessage("Created DATA table");
            }
        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        parent.newMessage("Opened DATA database successfully");
        return true;
    }

    public boolean dataEntry(String time, String mac) {

        try {
            stmt = c.createStatement();
            sqlCommand = "INSERT INTO DATA (TIMES, MAC) " +
                    "VALUES ( " + time + ", '" + mac + "' )";
            stmt.executeUpdate(sqlCommand);

        } catch (Exception e) {
            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

//    public boolean clientEntry(long clientID, String line) {
//
//        try {
//            stmt = c.createStatement();
//            sqlCommand = "INSERT INTO C" + String.valueOf(clientID) + " (ID, TIME, NAME, DATA) " +
//                    "VALUES ( " + line + " );";
//            stmt.executeUpdate(sqlCommand);
//
//        } catch (Exception e) {
//            parent.newMessage(e.getClass().getName() + ": " + e.getMessage());
//            return false;
//        }
//        return true;
//    }
}