import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Merger {

    public static void main(String[] args) {
        String sql;
        boolean creatingDB;

        //Declare database variables
        Connection c = null, c2 = null;
        Statement stmt = null;

        String s = System.getProperty("user.dir");

        //Check if the db file must be created
        File dbfile = new File(s + "\\test.db");
        creatingDB = !dbfile.exists();

        try {
            //Connect to the database
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);

            //Class.forName("org.sqlite.JDBC");
            c2 = DriverManager.getConnection("jdbc:sqlite:clicks.db");
            c2.setAutoCommit(false);

            System.out.println("Opened database successfully");

            //If the db file was created then create the table CLICKS
            if (creatingDB) {
                stmt = c.createStatement();
                sql = "CREATE TABLE TESTING (X DOUBLE, Y DOUBLE)";
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

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM TESTING;" );

            while (rs.next()) {
                double x = rs.getDouble("X");
                double y = rs.getDouble("Y");

                System.out.println("X: " + x + " Y: " + y);

                stmt = c2.createStatement();
                sql = "INSERT INTO TESTING (X, Y) VALUES (" + x + ", " + y + ");";
                stmt.executeUpdate(sql);
            }

//            stmt = c.createStatement();
//            sql = "INSERT INTO TESTING SELECT * FROM toMerge.TESTING";
//            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
            c2.commit();
            c2.close();
            System.out.println("Database closed");
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
