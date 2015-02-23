import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;


public class Main {

    public static void main(String[] args) throws Exception {

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:dbtest.db");
        Statement stat = conn.createStatement();

        //Remove Table if Exists
        stat.executeUpdate("drop table if exists sniffer;");

        //Create Table
        stat.executeUpdate("create table sniffer (second, microsecond, source, destination);");

        //Read in a CSV File
        BufferedReader br = new BufferedReader(new FileReader("snifflog_0.csv"));
        String line;

        //Entering Bogus first line
        PreparedStatement one = conn.prepareStatement("insert into sniffer values (?,?,?,?);");
        one.setString(1, "999");
        one.setString(2, "999");
        one.setString(3, "MAC");
        one.setString(4, "MAC");
        one.addBatch();

        one.executeBatch();
        //Result Set
        ResultSet rs = stat.executeQuery("SELECT * FROM sniffer;");

        while ( (line=br.readLine()) != null)
        {
            String[] values = line.split(",");    //your separator

            //String sql = "update people set firstname=? , lastname=? where id=?";

            //Check Database for the existing MAC

            String mac = rs.getString("source");
            System.out.println(mac);
            System.out.println(values[2]);
            if (mac .contains(values[2])) {

                System.out.println("Skip");

                //rs.next();
                //ps.close();
                //System.out.println("TIME: " + values[0]);
                //System.out.println("MAC: " + values[2]);
            }
            else{

                //Entering Data into Database
                PreparedStatement ps = conn.prepareStatement("insert into sniffer values (?,?,?,?);");

                ps.setString(1, values[0]);
                ps.setString(2, values[1]);
                ps.setString(3, values[2]);
                ps.setString(4, values[3]);
                ps.addBatch();

                ps.executeBatch();
                rs.next();
            }
        }
        br.close();

        //ResultSet rs = stat.executeQuery("select * from sniffer;");
        //while (rs.next()) {
        //   System.out.println("time = " + rs.getString("second"));
        //    System.out.println("MAC = " + rs.getString("source"));
        //}
        //rs.close();

        conn.close();

        System.out.println("Finished!");
    }
}
