import java.sql.*;

public class DBHandler implements Runnable {

    ClientRunnable parent;
    Connection c;
    Statement stmt;
    String sqlCommand;

    DBHandler(ClientRunnable parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        c = null;
        stmt = null;
        sqlCommand = null;

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:client.db");

            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "ROOT", null);

            if (tables.next()) {
                parent.updateUI("Table exists");

            } else {
                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE ROOT " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " CLIENTID           LONG    NOT NULL, " +
                        " NUMTABLES            INT     NOT NULL, " +
                        " TABLE1        CHAR(50));";
                stmt.executeUpdate(sqlCommand);

                parent.updateUI("Created ID table successfully");

                long clientID = new HighQualityRandom().nextLong();

                stmt = c.createStatement();
                sqlCommand = "INSERT INTO ROOT (ID, CLIENTID, NUMTABLES, TABLE1) " +
                        "VALUES (1, " + String.valueOf(Math.abs(clientID)) + ", 1, 'SINGLE' );";
                stmt.executeUpdate(sqlCommand);

                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE SINGLE " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " TIME           LONG    NOT NULL, " +
                        " NAME            CHAR(50)     NOT NULL, " +
                        " DATA        CHAR(50)      NOT NULL);";
                stmt.executeUpdate(sqlCommand);

                parent.updateUI("Created DEFAULT table successfully");
            }
        } catch (Exception e) {
            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
        }
        parent.updateUI("Opened database successfully");
    }

    public boolean commitData(long time, String name, String data) {

        try {
            stmt = c.createStatement();
            sqlCommand = "INSERT INTO SINGLE (TIME, NAME, DATA) " +
                    "VALUES ("+ String.valueOf(time) + ", '" + name + "', '" + data + "' );";
            stmt.executeUpdate(sqlCommand);

        } catch (Exception e) {
            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
            return false;

        } finally {
//            parent.updateUI("Write to DB successful");
            return true;
        }
    }

    public long getID() {
        ResultSet rs = null;
        long returnValue = 0;

        try {

            stmt = c.createStatement();
            sqlCommand = "SELECT CLIENTID FROM ROOT WHERE ID = 1;";
            rs = stmt.executeQuery(sqlCommand);
            returnValue = Long.valueOf(rs.getLong("CLIENTID"));

        } catch (Exception e) {
            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
            return 0;

        } finally {
            parent.updateUI("This client ID is: " + String.valueOf(returnValue));
            return returnValue;
        }
    }

    public long getLatestTime() {
        ResultSet rs = null;
        long returnValue = 0;

        try {

            stmt = c.createStatement();
            sqlCommand = "SELECT * FROM SINGLE ORDER BY ID DESC LIMIT 1";
            rs = stmt.executeQuery(sqlCommand);
            returnValue = Long.valueOf(rs.getLong("TIME"));

        } catch (Exception e) {
            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
            return 0;

        } finally {
            parent.updateUI("Latest entry timestamp: " + String.valueOf(returnValue));
            return returnValue;
        }
    }

    public ResultSet gatherDataForServer() {
        ResultSet rs = null;

        try {

            stmt = c.createStatement();
            sqlCommand = "SELECT * FROM SINGLE WHERE TIME > " + String.valueOf(parent.getServerTS());
            rs = stmt.executeQuery(sqlCommand);

        } catch (Exception e) {
            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
            return null;

        } finally {
            parent.updateUI("Sending entries to server...");
            return rs;
        }
    }
}