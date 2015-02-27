import java.io.File;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by kwilk_000 on 2/26/2015.
 */
public class dbMerger implements Runnable {

    static boolean transferConfirmation = false;
    static long lastTimeStamp;
    static String lastPBID;
    long x = 0;
    String y = null;
    long z = 0;

    private Socket socket;

    dbMerger (Socket socket){
        this.socket = socket;
    }
        public void run(){
            String sql;
            boolean creatingDB;

            //Declare database variables
            Connection c = null, c2 = null;
            Statement stmt = null;
            Statement stmt1 = null;

            String s = "D:\\Softwares\\SQLite";

            //Check if the db file must be created
            File dbfile = new File(s + "\\main.db");
            creatingDB = !dbfile.exists();

            try {
                //Connect to the database
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:"+s+"\\main.db"); //connects to main database.
                c.setAutoCommit(false);

                //Class.forName("org.sqlite.JDBC");
                c2 = DriverManager.getConnection("jdbc:sqlite:"+s+"\\AndroidTest.db"); //connects to received database
                c2.setAutoCommit(false);

                System.out.println("Opened database successfully");

                //Creates the table if the main database is being initially created.
                if (creatingDB) {
                    stmt = c.createStatement();
                    sql = "CREATE TABLE DATA (ID INTEGER PRIMARY KEY, TIMES LONG, MAC TEXT, PBID LONG)";
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

                stmt = c2.createStatement();
                stmt1 = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM DATA;" );//checks result set in table DATA from received database
                stmt = c2.createStatement();
                ResultSet root = stmt.executeQuery( "SELECT * FROM ROOT;" );//checks result set in table ROOT from received database
                ResultSet rsMain = stmt1.executeQuery("SELECT * FROM DATA;");    //begin checking result set in main database
                long main1 = 0;
                String main2 = null;
                long main3 = 0;
                z = root.getLong("PBID");

                int duplicateCheck = 0;
                while (rs.next()) {         //loops and moves cursor to next row until cursor hits the last row.
                    x = rs.getLong("TIMES");
                    y = rs.getString("MAC");
                     //reading data from android database.

                    System.out.println("TIMES: " + x + "\nMAC: " + y + "\nPBID: " + z);


                    rsMain.close();
                    rsMain = stmt1.executeQuery("SELECT * FROM DATA;"); //restarts result set checker for main database.

                    while (rsMain.next()) { //loops through main database.
                        main1 = rsMain.getLong("TIMES");
                        main2 = rsMain.getString("MAC");

                        if ((x == main1) && (y.equals(main2))) {   //checks main database for a possible duplicate entry before writing new data.
                            duplicateCheck = 1;
                            break;
                        }
                        else duplicateCheck = 0;
                    }

                    if (duplicateCheck == 0) {//if data to be entered to main database is not duplicate data, insert data into main database.

                        sql = "INSERT INTO DATA (TIMES, MAC, PBID) VALUES (" + x + ", " + y + ", " + z + ");";
                        stmt.executeUpdate(sql);

                    }

                }

                if (duplicateCheck==0){
                    lastTimeStamp = x;
                    lastPBID = y;
                }

                while(true) {
                    if (rsMain.next()) { //checks last line of main database

                        if ((x == main1) && (y.equals(main2)) && x!=0 && y!=null) {
                            transferConfirmation = true;
                            //checks
                            System.out.println("Transfer Confirmation true.");
                        }
                    }
                    else{
                        break;
                    }
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
