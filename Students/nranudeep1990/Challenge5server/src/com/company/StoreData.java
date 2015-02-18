package com.company;

import com.example.tungala.challenge5android.ClientInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by tungala on 2/5/2015.
 */
public class StoreData {

    public void insertData(ClientInfo ci) {

        Connection con = null;

        try {

            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement st1 = con.createStatement();
            st1.execute("create table IF NOT EXISTS "+ci.getUsername()+" (name TEXT,location TEXT,bssid TEXT,rssi INTEGER)");
            String query = "insert into "+ci.getUsername()+" values(?,?,?,?)";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1,ci.getUsername());
            st.setString(2,ci.getLocation());
            st.setString(3,ci.getWifiBssid());
            st.setInt(4,ci.getWifiRssid());
            int rs = st.executeUpdate();
            if(rs!=0) {
                System.out.println("Successfully inserted data of "+ci.getUsername());
            }
            else {
                System.out.println("Insertion failed for"+ci.getUsername());
            }

            st.close();
            con.close();


        }
        catch(Exception e) {
            e.printStackTrace();

        }
    }
}

