/**
 * Created by Benito on 2/6/2015.
 */
import java.sql.*;

public class Demo2 {
    public static void main(String[] args) throws Exception {
        // register the driver
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);

        // now we set up a set of fairly basic string variables to use in the body of the code proper
        String sTempDb = "hello.db";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + sTempDb;
        // which will produce a legitimate Url for SqlLite JDBC :
        // jdbc:sqlite:hello.db
        int iTimeout = 30;
        String sMakeTable = "CREATE TABLE demo (id numeric, response text)";
        String sMakeInsert = "INSERT INTO demo VALUES(1,'Hello from the Aggieland')";
        String sMakeSelect = "SELECT response from demo";

        // create a database connection
        Connection conn = DriverManager.getConnection(sDbUrl); //this makes a connection to the driver
        try {
            Statement stmt = conn.createStatement();// say on person has 10 connections this decides which connection to send it to
            try {
                stmt.setQueryTimeout(iTimeout);
                stmt.executeUpdate(sMakeTable);//this are your statement objects
                stmt.executeUpdate(sMakeInsert);
                ResultSet rs = stmt.executeQuery(sMakeSelect);// this is how java reads out an output from the connection result set object
                try {
                    while (rs.next()) {
                        String sResult = rs.getString("response");
                        System.out.println(sResult);
                    }
                } finally {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    }
                }
            } finally {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {
            }
        }
    }
}
