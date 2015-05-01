package com.google.android.gms.plus.sample.quickstart;//import com.sun.tools.javac.comp.Check;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by mandel on 4/15/15.
 */
public class DBHelper {
    Connection c;
    Statement stmt;
    
    public void DBHelper(){}
    public void openDB(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            stmt = c.createStatement();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }
    public void createDB(){
        try{
            String sql = "PRAGMA foreign_keys = ON";
            
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS 'USERS' (" +
                    "'ID' INTEGER PRIMARY KEY NOT NULL,"+
                    "'USERNAME' VARCHAR(45) NOT NULL,"+
                    "'NAME' VARCHAR(45) NOT NULL,"+
                    "'SESSION_ID' VARCHAR(45),"+
                    "'PASSWORD' VARCHAR(128) NOT NULL,"+
                    "'SALT' VARCHAR(45) NOT NULL)";

            stmt.executeUpdate(sql);


            sql = "CREATE TABLE IF NOT EXISTS 'FRIENDS' ("+
                    "'USERS_ID' INTEGER NOT NULL,"+
                    "'FRIEND_USERS_ID' INTEGER NOT NULL,"+
                    "PRIMARY KEY ('USERS_ID', 'FRIEND_USERS_ID'),"+
                    "FOREIGN KEY ('USERS_ID')"+
                    "REFERENCES 'USERS' ('ID')" +
                    "ON DELETE CASCADE,"+
                    "FOREIGN KEY ('FRIEND_USERS_ID')"+
                    "REFERENCES 'USERS' ('ID')" +
                    "ON DELETE CASCADE)";

            stmt.executeUpdate(sql);


            sql = "CREATE TABLE IF NOT EXISTS 'LOCATIONS' ("+
                    "'ID' INTEGER PRIMARY KEY NOT NULL,"+
                    "'NAME' VARCHAR(45) NOT NULL,"+
                    "'LATITUDE' REAL,"+
                    "'LONGITUDE' REAL,"+
                    "'IMAGE' VARCHAR(45))";

            stmt.executeUpdate(sql);


            sql = "CREATE TABLE IF NOT EXISTS 'CHECKINS' ("+
                    "'USERS_ID' INTEGER NOT NULL,"+
                    "'LOCATIONS_ID' INTEGER NOT NULL,"+
                    "'TIMESTAMP' INTEGER NOT NULL,"+
                    "'METHOD' VARCHAR(45),"+
                    "PRIMARY KEY ('USERS_ID', 'TIMESTAMP'),"+
                    "FOREIGN KEY ('USERS_ID')"+
                    "REFERENCES 'USERS' ('ID') ON DELETE CASCADE,"+
                    "FOREIGN KEY ('LOCATIONS_ID')"+
                    "REFERENCES 'LOCATIONS' ('ID') ON DELETE CASCADE )";

            stmt.executeUpdate(sql);


/*
            sql = "CREATE TABLE IF NOT EXISTS 'REQUESTS' ("+
                    "'USERS_ID' INTEGER NOT NULL,"+
                    "'FRIEND_USERS_ID' INTEGER NOT NULL,"+
                    "PRIMARY KEY ('USERS_ID', 'FRIEND_USERS_ID'),"+
                    "FOREIGN KEY ('USERS_ID')"+
                    "REFERENCES 'USERS' ('ID') "+
                    "ON DELETE CASCADE ,"+
                    "FOREIGN KEY ('FRIEND_USERS_ID')"+
                    "REFERENCES 'USERS' ('ID') " +
                    "ON DELETE CASCADE )";


            stmt.executeUpdate(sql);
*/
        }
        
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public void closeDB(){  
        try {
            stmt.close();
            c.close();
        }
        catch(Exception e){
            e.printStackTrace();
            
        }
            
    }
    public void addUser(String userName, String name, String sessionID, String password, String salt ){ //set userID to -1 if adding
            try{
                String sql;
                sql = "INSERT INTO USERS (USERNAME, NAME, SESSION_ID, PASSWORD, SALT) VALUES ('"+
                        userName + "', '"+ name +"', '"+sessionID+"', '" + password + "', '" + salt+ "')";
                
                stmt.executeUpdate(sql);

            }

            catch(Exception e){
                e.printStackTrace();
            }
        }
    public void deleteUser(int userID){
        try{
            String sql;
            sql = "DELETE FROM USERS WHERE ID ="+userID;

            stmt.executeUpdate(sql);

        }

        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public void addLocation(String name, double latitude, double longitude, String image){
        try{
            String sql;
            sql = "INSERT INTO LOCATIONS (NAME, LATITUDE, LONGITUDE, IMAGE) VALUES ('"+
                    name + "', "+latitude+", "+longitude+", '" + image + "')";

            stmt.executeUpdate(sql);

        }

        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public void deleteLocation(int locationID){
        try{
            String sql;
            sql = "DELETE FROM LOCATIONS WHERE ID ="+locationID;

            stmt.executeUpdate(sql);

        }

        catch(Exception e){
            e.printStackTrace();
        }


    }
    public void addFriend(int userID, int friendID){
        try{
            String sql;
            sql = "INSERT INTO FRIENDS (USERS_ID, FRIEND_USERS_ID) VALUES ("+
                    userID+","+friendID+")";
            
            stmt.executeUpdate(sql);
            
            sql = "INSERT INTO FRIENDS (USERS_ID, FRIEND_USERS_ID) VALUES ("+
                    friendID+","+userID+")";

            stmt.executeUpdate(sql);

        }

        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteFriend(int userID, int friendID){
        try{
            String sql;
            sql = "DELETE FROM FRIENDS WHERE USERS_ID ="+userID+" AND FRIEND_USERS_ID ="+friendID;

            stmt.executeUpdate(sql);

            sql = "DELETE FROM FRIENDS WHERE USERS_ID ="+friendID+" AND FRIEND_USERS_ID ="+userID;

            stmt.executeUpdate(sql);
        }

        catch(Exception e){
            e.printStackTrace();
        }


    }
    public void checkIn(int userID, int locationID, int timestamp, String method){
        try{
            String sql;
            sql = "INSERT INTO CHECKINS (USERS_ID, LOCATIONS_ID, TIMESTAMP , METHOD) VALUES ("+
                    userID+","+locationID+","+timestamp+", '"+method+"')";

            stmt.executeUpdate(sql);

        }

        catch(Exception e){
            e.printStackTrace();
        }


    }
    public String getUserName(int userID){
        try{
            String sql;
            sql = "SELECT USERNAME FROM USERS WHERE ID="+userID;

            ResultSet rs = stmt.executeQuery(sql);
            return rs.getString("USERNAME");

        }

        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String getLocationName(int locID){
        try{
            String sql;
            sql = "SELECT NAME FROM LOCATIONS WHERE ID="+locID;

            ResultSet rs = stmt.executeQuery(sql);
            return rs.getString("NAME");

        }

        catch(Exception e){
            e.printStackTrace();
        }
        return null;
        
    }
    public int getUserIDByUserName(String userName){ //identical usernames fail
        int userID = -1;
        try {
            ResultSet rs = stmt.executeQuery("SELECT ID, USERNAME FROM USERS WHERE USERNAME ='" + userName+"'");
            userID = rs.getInt("ID");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userID;
        
    }
    public int getUserIDByName(String name){ //identical Names fail
        int userID = -1;
        try {
            ResultSet rs = stmt.executeQuery("SELECT ID, NAME FROM USERS WHERE NAME ='" + name+"'");
            userID = rs.getInt("ID");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userID;

    }
    public int getLocationIDByName(String locationName){//identical location names fail
        int locID = -1;
        try {
            ResultSet rs = stmt.executeQuery("SELECT ID, NAME FROM LOCATIONS WHERE NAME ='" + locationName+"'");
            locID = rs.getInt("ID");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return locID;
        
    }
    public int getUserIDBySessionID(String sessionID){
        int userID = -1;
        try {
            ResultSet rs = stmt.executeQuery("SELECT ID, USERNAME FROM USERS WHERE SESSION_ID ='" + sessionID+"'");
            userID = rs.getInt("ID");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userID;

    }
    public ArrayList<String> getFriends(int userID){
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT USERNAME FROM USERS INNER JOIN FRIENDS ON FRIENDS.USERS_ID ="+userID+" AND USERS.ID = FRIENDS.FRIEND_USERS_ID");
            while(rs.next()){
                String username = rs.getString("USERNAME");
                arrayList.add(username);

            }
            return arrayList;
            
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<CheckIn> getCheckInByLocation(int locationID){
        ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT USERS_ID,TIMESTAMP, METHOD FROM CHECKINS WHERE LOCATIONS_ID ="+locationID);
            while(rs.next()){
                int userID = rs.getInt("USERS_ID");
                String username = getUserName(userID);
                String location = getLocationName(locationID);
                int timestamp = rs.getInt("TIMESTAMP");
                String method = rs.getString("METHOD");
                CheckIn ci = new CheckIn(username,location,timestamp,method);
                checkIns.add(ci);
            }
            return checkIns;

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
        
        
    }
    public ArrayList<CheckIn> getCheckInByUser(int userID){
        ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT USERS_ID, LOCATIONS_ID, TIMESTAMP, METHOD FROM CHECKINS WHERE USERS_ID ="+userID);
            while(rs.next()){
                int locID = rs.getInt("LOCATIONS_ID");
                String username = getUserName(userID);
                String location = getLocationName(locID);
                int timestamp = rs.getInt("TIMESTAMP");
                String method = rs.getString("METHOD");
                CheckIn ci = new CheckIn(username,location,timestamp,method);
                checkIns.add(ci);
            }
            return checkIns;

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;



    }
    
}
