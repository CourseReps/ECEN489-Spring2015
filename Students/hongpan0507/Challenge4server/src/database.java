/**
 * Created by hpan on 2/1/15.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class database {
    private String driver = "org.sqlite.JDBC";
    private Connection db_con = null;
    private String db_name = "";
    private Statement db_stmt = null;
    private String sql = "";

    private String time = "";
    private String x_loc = "";
    private String y_loc = "";

    //constructor
    public database (String _db_name) {
        db_name = "jdbc:sqlite:/home/hpan/workspace_sqlite/" + _db_name;
    }

    public void open_db() throws Exception {
        //STEP 1: Register JDBC driver
        Class.forName(driver);
        //STEP 2: Create connection to database
        db_con = DriverManager.getConnection(db_name);
        db_con.setAutoCommit(false);
        db_stmt = db_con.createStatement();
    }
    public void create_table(String _table_name) throws Exception {
        //STEP 3: Execute SQL query using Statement object
        sql = "Create Table " + _table_name + " " +
                "(TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "X INT NOT NULL, " +
                "Y INT NULL)";
        db_stmt.executeUpdate(sql);
        db_con.commit();
    }
    public void read_table_name() throws  Exception {

    }
    public void insert_val(String _table_name, int _x_loc, int _y_loc) throws Exception {
        x_loc = Integer.toString(_x_loc) + ", ";
        y_loc = Integer.toString(_y_loc);

        sql = "INSERT INTO " + _table_name + " " +
              "(TIME,X,Y) " +
                "VALUES (" + "time('now'), " + x_loc + y_loc + " );";
        db_stmt.executeUpdate(sql);
        db_con.commit();
    }
    public void close_db() throws Exception {
        db_stmt.close();
        db_con.close();
    }
}
