
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DatabaseManager {
    //initialize connection and statement variables
    Connection connection = null;
    Statement statement = null;
    PreparedStatement pState = null;
    boolean hasUpdates = false;


    final String DB_NAME = "regression.db";
    final String TABLE_NAME = "DATA2";

    //constructor used to initialize connection to database
    DatabaseManager () {
        initialize();
    }

    private void initialize() {
        try {
            //import class and connect to existing database/create database
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\SQLite\\" + DB_NAME);
            statement = connection.createStatement();
            System.out.println("Connected to Database!");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method used to check for entries needed to be pushed to Fusion Table and sets corresponding flag
    public void checkAdded () {
        try {
            ResultSet set = statement.executeQuery("SELECT * FROM " + TABLE_NAME + " WHERE ADDED = 'NO';");
            if (set.next()) {
                hasUpdates = true;
            }
            else {
                hasUpdates = false;
            }
        }

        catch (SQLException s) {
            s.getMessage();
        }
    }

    public void getData () {
        try {
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_NAME + " WHERE ADDED = 'NO';");
            SimpleDateFormat fusionFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            boolean added = false;
            while (results.next()) {
                    String date = fusionFormat.format(results.getLong("TIME") * 1000);
                    int num = results.getInt("NUM_PEOPLE");
                    int id = results.getInt("PBID");
                    added = ConnectToFusion.insertData(date, num, id);

                    //if insert operation succeeds, update its 'ADDED' value
                    if (added)
                        connection.createStatement().executeUpdate("UPDATE " + TABLE_NAME + " SET ADDED = 'YES' WHERE TIME = " + results.getLong("TIME") + ";");
                    Thread.sleep(1000);
            }
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
        catch (IOException i) {
            i.getMessage();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
