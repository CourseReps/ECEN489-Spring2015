import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * Created by RhoadsWylde on 4/22/2015.
 */
public class DBConnect {

        public String DBConnect()
        {
            Connection connection = null;
            ResultSet resultSet = null;
            Statement statement = null;
            String connected;

            try
            {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\RhoadsWylde\\Master Clone\\Misc Coding Projects\\Project 3\\Main.db");
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT ID FROM USERS");
                connected = "yes";
                while (resultSet.next())
                {
                    System.out.println("USER NAME:"
                            + resultSet.getString("ID"));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                connected = "no";
            }
            finally
            {
                try
                {
                    resultSet.close();
                    statement.close();
                    connection.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return connected;
        }
    }

