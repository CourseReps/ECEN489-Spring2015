import java.io.File;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by kwilk_000 on 2/26/2015.
 */
public class dbMerger {

    static boolean transferConfirmation = false;
    static int lastRowID;
    static int lastPBID;
    long x = 0;
    String y = null;
    int z = 0;

    private Socket socket;

  /*  dbMerger (Socket socket){
        this.socket = socket;
    }*/
        public void dbMerge (){
            String sql;
            boolean creatingDB;

            //Declare database variables
            Connection mainConnect = null, receivedConnect = null;
            Statement stmt = null;
            Statement stmt1 = null;

            String addDB = dbAcceptor.dbReceived;

            String s = System.getProperty("user.dir");

            //Check if the db file must be created
            /*File dbfile = new File(s + "\\main.db");
            creatingDB = !dbfile.exists();*/

            try {
                //Connect to the database
                Class.forName("org.sqlite.JDBC");
                mainConnect = DriverManager.getConnection("jdbc:sqlite:"+s+"\\main.db"); //connects to main database.
                mainConnect.setAutoCommit(false);

                //Class.forName("org.sqlite.JDBC");
                receivedConnect = DriverManager.getConnection("jdbc:sqlite:"+addDB); //connects to received database
                receivedConnect.setAutoCommit(false);

                System.out.println("Opened database successfully");

                //Creates the table if the main database is being initially created.
                try {
                    stmt = mainConnect.createStatement();
                    sql = "CREATE TABLE DATA (ID INTEGER PRIMARY KEY, TIME LONG, MAC TEXT, PBID INTEGER)";
                    System.out.println("data table created.");
                    stmt.executeUpdate(sql);
                }
                catch(Exception e)
                {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage());
                }
                /*if (creatingDB) {
                    stmt = c.createStatement();
                    sql = "CREATE TABLE DATA (ID INTEGER PRIMARY KEY, TIMES LONG, MAC TEXT, PBID LONG)";
                    stmt.executeUpdate(sql);
                }*/

                //Get Points from the client and load them into the database
//            for (int i = 0; i < 10; i++) {
//                stmt = c.createStatement();
//                sql = "INSERT INTO TESTING (X, Y) VALUES (" + i + ", " + i + ");";
//                stmt.executeUpdate(sql);
//
//                //System.out.println("Point (" + p.getX() + ", " + p.getY() + ") received from client and loaded into database");
//            }
                System.out.println("check 0");

                stmt = receivedConnect.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM DATA;" );//checks result set in table DATA from received database
                System.out.println("check 1");

               /* stmt = receivedConnect.createStatement();
                System.out.println("check 2");
                ResultSet root = stmt.executeQuery( "SELECT * FROM PBID;" );//checks result set in table ROOT from received database*/


                //todo the root table will be named "PBID" WITH AN ID COLUMN WITH DATA TYPE INTEGER
                //TODO THE TIMES COLUMN WILL BE NAMED "TIME" WITH DATA TYPE LONG

/*                stmt1 = mainConnect.createStatement();
                ResultSet minRowIDMain = stmt1.executeQuery("SELECT ID FROM DATA ORDER BY ID DESC LIMIT 1;");
                if(minRowIDMain.next())
                    z = minRowIDMain.getInt(1);
                minRowIDMain.close();*/

//                minRowIDMain.next();
//                z = minRowIDMain.getInt("ID");//Deletes any rows with same entries in timestamp and mac address columns

                System.out.println("check 3");
                stmt1 = mainConnect.createStatement();
                ResultSet rsMain = stmt1.executeQuery("SELECT * FROM DATA;");    //begin checking result set in main database
                System.out.println("check 4");

                long main1 = 0;
                //System.out.println("check 5");
                String main2 = null;

               // z = root.getInt("ID");
                System.out.println("check 5");
                int duplicateCheck = 0;
                int i = 0;
                /*stmt = receivedConnect.createStatement();
                stmt1 = mainConnect.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM DATA;" );//checks result set in table DATA from received database
                stmt = receivedConnect.createStatement();
                ResultSet root = stmt.executeQuery( "SELECT * FROM ROOT;" );//checks result set in table ROOT from received database
                ResultSet rsMain = stmt1.executeQuery("SELECT * FROM DATA;");    //begin checking result set in main database
                long main1 = 0;
                String main2 = null;
                long main3 = 0;
                z = root.getLong("PBID");

                int duplicateCheck = 0;*/


                while (rs.next()) {         //loops and moves cursor to next row until cursor hits the last row.
                    x = rs.getLong("TIMES");
                    y = rs.getString("MAC");
                    i = rs.getInt("ID");
                     //reading data from android database.
                   // System.out.println("TIMES: " + x + "\nMAC: " + y + "\nPBID: " + z);
                    /*rsMain.close();
                    rsMain = stmt1.executeQuery("SELECT * FROM DATA;"); //restarts result set checker for main database.*/
                    rsMain.close();
                    stmt1 = mainConnect.createStatement();
                    rsMain = stmt1.executeQuery("SELECT * FROM DATA;"); //restarts result set checker for main database.
                   /* while (rsMain.next()) { //loops through main database.
                        main1 = rsMain.getLong("TIMES");
                        main2 = rsMain.getString("MAC");
                        if ((x >= (main1 - 1000)) && (x <= (main1 + 1000)) && (y.equals(main2))) {   //checks main database for a possible duplicate entry before writing new data.
                            duplicateCheck = 1;
                            break;
                        }
                        else duplicateCheck = 0;
                    }*/
                    if (duplicateCheck == 0) {//if data to be entered to main database is not duplicate data, insert data into main database.
                        /*sql = "INSERT INTO DATA (TIMES, MAC, PBID) VALUES (" + x + ", " + y + ", " + z + ");";
                        stmt.executeUpdate(sql);*/
                        stmt1 = mainConnect.createStatement();
                        sql = "INSERT INTO DATA (TIME,MAC,PBID) VALUES (" + x + ", '" + y + "', 1);";
                        stmt1.executeUpdate(sql);
                    }
                }//  if (duplicateCheck==0){
                    lastRowID = i;
                    lastPBID = 1;//}

               /* ResultSet lastPrimaryKey = stmt1.executeQuery("SELECT * FROM DATA;");
                int lastID = 0;
//                while(lastPrimaryKey.next()){
//                    lastID = lastPrimaryKey.getInt("ID");
//                }
                stmt = mainConnect.createStatement();
                lastPrimaryKey = stmt.executeQuery("SELECT ID FROM DATA ORDER BY ID DESC LIMIT 1;");

                if(lastPrimaryKey.next())
                    lastID = lastPrimaryKey.getInt(1);*/

                stmt1 = mainConnect.createStatement();
                sql = "DELETE FROM DATA WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM DATA GROUP BY TIME, MAC);";
                stmt1.executeUpdate(sql);//Deletes any rows with same entries in timestamp and mac address columns
                System.out.println("Duplicate rows deleted if any exist in main database.");

                /*stmt1 = mainConnect.createStatement();
                sql = "VACUUM DATA;";
                stmt1.executeUpdate(sql);*/

                //TODO NEED TO CREATE CHECK TO CONFIRM THAT DATA WAS WRITTEN FROM THE INCOMING DATABASE TO THE MAIN DATABASE
                //TODO AFTER CONFIRMATION IS COMPLETE, WRITE CODE TO DELETE FILE AND SEND CONFIRMATION TO ANDROID.
         /*       while(true) {
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
                }*/
//                stmt1 = mainConnect.createStatement();
                ResultSet lastPrimaryKey = stmt1.executeQuery("SELECT * FROM DATA;");
                int lastID = 0;
//                while(lastPrimaryKey.next()){
//                    lastID = lastPrimaryKey.getInt("ID");
//                }
                stmt = mainConnect.createStatement();
                lastPrimaryKey = stmt.executeQuery("SELECT ID FROM DATA ORDER BY ID DESC LIMIT 1;");

                if(lastPrimaryKey.next())
                    lastID = lastPrimaryKey.getInt(1);

                System.out.println(lastID);
                System.out.println("check 7 ");

                if(lastRowID==(lastID)) {
                    transferConfirmation = true;
                }
                else transferConfirmation = false;
                //checks
                System.out.println("Transfer Confirmation true.");

                //TODO USE SELECT * FROM TABLE WHERE.
//            stmt = c.createStatement();
//            sql = "INSERT INTO TESTING SELECT * FROM toMerge.TESTING";
//            stmt.executeUpdate(sql);
                stmt.close();
                mainConnect.commit();
                mainConnect.close();
                receivedConnect.commit();
                receivedConnect.close();
                System.out.println("Database closed");
            }
            catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }


        }



}
