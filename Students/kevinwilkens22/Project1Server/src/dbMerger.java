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

        public void dbMerge (){
            String sql;
            boolean creatingDB;

            //Declare database variables
            Connection mainConnect = null, receivedConnect = null;
            Statement stmt = null;
            Statement stmt1 = null;

            String addDB = dbAcceptor.dbReceived;

            String s = System.getProperty("user.dir");

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
                    sql = "CREATE TABLE DATA (PB_DATA_ID INTEGER, TIME LONG, MAC TEXT, PBID INTEGER)";
                    System.out.println("data table created.");
                    stmt.executeUpdate(sql);
                }
                catch(Exception e)
                {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage());
                }


                System.out.println("check 0");

                stmt = receivedConnect.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM DATA;" );//checks result set in table DATA from received database
                System.out.println("check 1");

                stmt = receivedConnect.createStatement();
                System.out.println("check 2");
                ResultSet root = stmt.executeQuery( "SELECT * FROM PBID;" );//checks result set in table ROOT from received database

                //todo the root table will be named "PBID" WITH AN ID COLUMN WITH DATA TYPE INTEGER
                //TODO THE TIMES COLUMN WILL BE NAMED "TIME" WITH DATA TYPE LONG


                System.out.println("check 3");
                stmt1 = mainConnect.createStatement();
                ResultSet rsMain = stmt1.executeQuery("SELECT * FROM DATA;");    //begin checking result set in main database
                System.out.println("check 4");

                long main1 = 0;
                //System.out.println("check 5");
                String main2 = null;

                root.next();
                z = root.getInt("ID");
                System.out.println("check 5");
                int duplicateCheck = 0;
                int i = 0;



                while (rs.next()) {         //loops and moves cursor to next row until cursor hits the last row.
                    x = rs.getLong("TIME");
                    y = rs.getString("MAC");
                    i = rs.getInt("ID");

                    rsMain.close();
                    stmt1 = mainConnect.createStatement();
                    rsMain = stmt1.executeQuery("SELECT * FROM DATA;"); //restarts result set checker for main database.

                    if (duplicateCheck == 0) {  //if data to be entered to main database is not duplicate data, insert data into main database.

                        stmt1 = mainConnect.createStatement();
                        sql = "INSERT INTO DATA (PB_DATA_ID,TIME,MAC,PBID) VALUES ("+i+", " + x + ", '" + y + "', " + z +");";
                        stmt1.executeUpdate(sql);
                    }
                }//  if (duplicateCheck==0){
                    lastRowID = i;
                    lastPBID = z;//}

                stmt1 = mainConnect.createStatement();
                sql = "DELETE FROM DATA WHERE ROWID NOT IN (SELECT MIN(ROWID) FROM DATA GROUP BY TIME, MAC);";
                stmt1.executeUpdate(sql);//Deletes any rows with same entries in timestamp and mac address columns
                System.out.println("Duplicate rows deleted if any exist in main database.");

                //TODO NEED TO CREATE CHECK TO CONFIRM THAT DATA WAS WRITTEN FROM THE INCOMING DATABASE TO THE MAIN DATABASE
                //TODO AFTER CONFIRMATION IS COMPLETE, WRITE CODE TO DELETE FILE AND SEND CONFIRMATION TO ANDROID.

                ResultSet lastPrimaryKey = stmt1.executeQuery("SELECT * FROM DATA;");
                int lastID = 0;

                stmt = mainConnect.createStatement();
                lastPrimaryKey = stmt.executeQuery("SELECT PB_DATA_ID FROM DATA ORDER BY PB_DATA_ID DESC LIMIT 1;");

                if(lastPrimaryKey.next())
                    lastID = lastPrimaryKey.getInt(1);

                System.out.println(lastID);
                System.out.println("check 7 ");


                System.out.println("lastID = " + lastID + "\nlastRowID = "+ lastRowID);

                transferConfirmation = true;

                //TODO USE SELECT * FROM TABLE WHERE.

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
