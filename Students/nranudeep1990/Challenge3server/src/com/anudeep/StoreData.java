package com.anudeep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by tungala on 2/5/2015.
 */
public class StoreData {

    public void insertData(ClientInfo ci) {

        Connection con = null;

        try {

            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:C:/Users/tungala/Desktop/Mobile Sensing/test.db");
            String query = "insert into clientdata(clientnumber,country,osarch,osname,userhome,username,javaversion) values(?,?,?,?,?,?,?)";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1,ci.getClientNumber());
            st.setString(2,ci.getCountry());
            st.setString(3,ci.getOsArch());
            st.setString(4,ci.getOsName());
            st.setString(5,ci.getUserHome());
            st.setString(6,ci.getUserName());
            st.setString(7,ci.getJavaVersion());

            int rs = st.executeUpdate();
            if(rs!=0) {
                System.out.println("Successfully inserted data of "+ci.getUserName());
            }
            else {
                System.out.println("Insertion failed for"+ci.getUserName());
            }

            st.close();
            con.close();


        }
        catch(Exception e) {
            e.printStackTrace();

        }
    }
}
