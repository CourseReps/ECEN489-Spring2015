import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;

public class SqlDatabase {

    MainRunnable parent;

    Connection c;
    Statement stmt;
    String sqlCommand;

    SqlDatabase(MainRunnable parent) {

        this.parent = parent;

        c = null;
        stmt = null;
        sqlCommand = null;

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:megasnif.db");

            DatabaseMetaData dbm = c.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "MEGASNIF", null);

            if (tables.next()) {
                System.out.print("MEGASNIF Table exists\n");
                return;

            } else {
                stmt = c.createStatement();
                sqlCommand = "CREATE TABLE MEGASNIF " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " timemillis       LONG     NOT NULL, " +
                        " mac              CHAR(50) NOT NULL," +
                        " rssi             INT      NOT NULL," +
                        " leftant          INT      NOT NULL," +
                        " rightant         INT      NOT NULL" +
                        ")";
                stmt.execute(sqlCommand);

                System.out.print("Created MEGASNIF table");
            }
        } catch (Exception e) {
            System.out.print(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.print("Opened MEGASNIF database successfully\n");
        return;
    }


    public void writeLine(PacketInfo info) {

        String line = info.timeMillis + ", " +
                "\"" + info.MAC + "\"" + ", " +
                info.signalStrength + ", " +
                parent.getSocket().leftAnt + ", " +
                parent.getSocket().rightAnt;

//        System.out.print(line + "\n");

        try {
            stmt = c.createStatement();

            sqlCommand = "INSERT INTO MEGASNIF (timemillis, mac, rssi, leftant, rightant) " +
                    "VALUES ( " + line + " )";

            stmt.execute(sqlCommand);


        } catch (Exception e) {
            System.out.print(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}