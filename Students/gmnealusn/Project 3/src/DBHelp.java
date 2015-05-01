import javax.swing.*;
import java.sql.*;

/**
 * Created by RhoadsWylde on 4/22/2015.
 */
public class DBHelp {
    public String dataBase = "org.sqlite.JDBC";
    //public String filePath = "jdbc:sqlite:C:\\Users\\RhoadsWylde\\Master Clone\\Misc Coding Projects\\Project 3\\Main.db";
    public String filePath = "jdbc:sqlite:\\home\\blade\\Project3\\Database\\Project3Test.db";
    Connection serverConnection = null;
    Connection DBconnection = null;
    public ResultSet rSet = null;
    PreparedStatement state = null;
    Statement stmt = null;
    private Credentials credentials;
    private System_Admin sysAdd;
    private static boolean svrConnected;
    private static boolean dbConnected;
    ////////////////////////////////////////////////////////////////////
    public boolean serverConnect() {
        try {
            svrConnected = true;
            credentials.serverConnected();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            svrConnected = false;
            e.printStackTrace();
        }
        return svrConnected;
    }
////////////////////////////////////////////////////////////////////
    public boolean DBConnect() {
            try {
                Class.forName(dataBase);
                DBconnection = DriverManager.getConnection(filePath);
                stmt = DBconnection.createStatement();
                dbConnected = true;
                //sysAdd.DBisConnected();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                dbConnected = false;
            }
            return dbConnected;
        }
////////////////////////////////////////////////////////////////////
    public ResultSet getData() {
        if (System_Admin.selectedTable.equals("Users")) {
            try {
                state = DBconnection.prepareStatement("Select * from USERS;");
                rSet = state.executeQuery();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }
        }
        else if (System_Admin.selectedTable.equals("Locations")) {
            try {
                state = DBconnection.prepareStatement("Select * from LOCATIONS;");
                rSet = state.executeQuery();
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }
        }
        return rSet;
    }
////////////////////////////////////////////////////////////////////
    public void addUser(String userName, String name,
                        String sessionID, String password, String salt ) { //set userID to -1 if adding
        try {
            String sql;
            sql = "INSERT INTO USERS (USERNAME, NAME, SESSION_ID, PASSWORD, SALT) VALUES ('"+
            userName + "', '"+ name +"', '"+sessionID+"', '" + password + "', '" + salt+ "')";
            stmt.executeUpdate(sql);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userID) {
        try {
            String sql;
            sql = "DELETE FROM USERS WHERE ID ="+userID;
            stmt.executeUpdate(sql);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addLocation(String name, double latitude, double longitude, String image) {
        try {
            String sql;
            sql = "INSERT INTO LOCATIONS (NAME, LATITUDE, LONGITUDE, IMAGE) VALUES ('"+
                    name + "', "+latitude+", "+longitude+", '" + image + "')";
            stmt.executeUpdate(sql);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteLocation(int locationID) {
        try {
            String sql;
            sql = "DELETE FROM LOCATIONS WHERE ID ="+locationID;
            stmt.executeUpdate(sql);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////
    public boolean DBClose() {
        dbConnected = false;

        return dbConnected;
    }
//////////////////////////////////////////////////////
    public static boolean dbConn(){
        return dbConnected;
    }
//////////////////////////////////////////////////////
    public static boolean svrConn(){
        return svrConnected;
    }
//////////////////////////////////////////////////////
    public void addCredentials(Credentials cred) {
        this.credentials = cred;
    }
//////////////////////////////////////////////////////
    public void addSysAdmin(System_Admin sa) {
        this.sysAdd = sa;
    }
}

