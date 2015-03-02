import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class Merger {

    public static void main(String[] args) {
        String sql;
        boolean creatingDB;
        String newDB = "test2.db";
        String finalDB = "final.db";

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //Declare database variables
        Connection dbIN, dbOUT, dbFinal;
        Statement stmt;

        String s = System.getProperty("user.dir");

        //Check if the db file must be created
        File dbfile = new File(s + "\\" + newDB);
        creatingDB = !dbfile.exists();

        try {
            //Connect to the database
            Class.forName("org.sqlite.JDBC");
            dbIN = DriverManager.getConnection("jdbc:sqlite:testing.db");
            dbIN.setAutoCommit(false);

            //Class.forName("org.sqlite.JDBC");
            dbOUT = DriverManager.getConnection("jdbc:sqlite:" + newDB);
            dbOUT.setAutoCommit(false);

            //Class.forName("org.sqlite.JDBC");
            dbFinal = DriverManager.getConnection("jdbc:sqlite:" + finalDB);
            dbFinal.setAutoCommit(false);

            System.out.println("Opened databases successfully");

            //If the db file was created then create the table CLICKS
            if (creatingDB) {
                stmt = dbOUT.createStatement();
                //sql = "CREATE TABLE DATA (ID INTEGER PRIMARY KEY, TIMES LONG, MAC TEXT, PBID LONG)";
                sql = "CREATE TABLE DATA (PBID INTEGER, TIME LONG, MAC TEXT)";
                stmt.executeUpdate(sql);
            }

            //Get Points from the client and load them into the database
//            for (int i = 0; i < 10; i++) {
//                stmt = c.createStatement();
//                sql = "INSERT INTO TESTING (X, Y) VALUES (" + i + ", " + i + ");";
//                stmt.executeUpdate(sql);
//
//                //System.out.println("Point (" + p.getX() + ", " + p.getY() + ") received from client and loaded into database");
//            }

            //Get the PBID which indicates which PB the data came from
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


            //long pbid = 1000;
            long time;
            String mac;

            /*
            Get min time
            while
             */

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
    }
}
