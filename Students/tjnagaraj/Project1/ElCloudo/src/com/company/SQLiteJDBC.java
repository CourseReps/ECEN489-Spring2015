package com.company;
import java.io.IOException;
import java.sql.*;

/**
 * Created by NAGARAJ on 3/1/2015.
 */
public class SQLiteJDBC
{
    Connection connection = null;
    Statement statement =null;
    String tableName;
    public SQLiteJDBC(String tableName)
    {
        this.tableName=tableName;
        try {
            Class.forName("org.sqlite.JDBC");
            connection= DriverManager.getConnection("jdbc:sqlite:final.db");
            System.out.println("Successfully opened database");
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+":"+e.getMessage());
            System.exit(0);
        }

    }
    public void PushAddedNoFusionTables(String tableId, FusionTablesHelper fusionTablesHelper) {

        try {

            statement = connection.createStatement();
            String sql = "SELECT * FROM " + tableName + " WHERE ADDED='YES'";
            ResultSet rs = statement.executeQuery(sql);
            int count = 0;
            boolean finish=true;
            while (finish) {
                finish=rs.next();
                if(finish==false)
                return;
                count++;
                FusionTableRow row = new FusionTableRow();
                row.setTime(rs.getString("TIME"));
                row.setNum_people(rs.getInt("NUM_MACS"));
                row.setPbid(rs.getInt("PBID"));
                fusionTablesHelper.insertData(tableId, row);
                String sql2 = "UPDATE " + tableName + " SET ADDED='NO' WHERE TIME= '" + rs.getString("TIME") + "'";
                Statement stmt2 = connection.createStatement();
                stmt2.execute(sql2);
                stmt2.close();
            }


        } catch (SQLException e) {

            System.out.println("SQL Exception");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        } catch (IOException e) {

            try {

                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            //e.printStackTrace();
        }

    }
    public void closeJDBCConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
