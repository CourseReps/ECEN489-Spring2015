/**
 * Created by hpan on 2/1/15.
 */

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class database {
    private String driver = "org.sqlite.JDBC";
    private Connection db_con = null;
    private String db_name = "";
    private Statement db_stmt = null;
    private String sql = "";
    private String table_name;
    private String[] column_name;
    private String[] column_type;

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
    public void create_table(String _table_name, String[] _column_name, String[] _column_type) throws Exception {
        table_name = _table_name;
        column_type = _column_type;
        column_name = _column_name;
        //STEP 3: Execute SQL query using Statement object
        sql = "Create Table If Not Exists " + table_name + " " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP";
        for (int i = 0; i < column_name.length; ++i) {
            sql = sql + ", " + column_name[i] + " " + column_type[i];
        }
        sql = sql + ");";
        //System.out.println(sql);    //for debugging only

        db_stmt.executeUpdate(sql);
        db_con.commit();
    }
    public void insert_val(String _table_name, String[] data) throws Exception {
        sql = "INSERT INTO " + _table_name + " (TIME";
        for (int i = 0; i < data.length; ++i) {
            sql = sql + "," + column_name[i];
        }
        sql = sql + ")VALUES (time('now')";
        for (int i = 0; i < data.length; ++i) {
            sql = sql + "," + data[i];
        }
        sql = sql + ");";
        //System.out.println(sql);    //for debugging only

        db_stmt.executeUpdate(sql);
        db_con.commit();
    }
    public void close_db() throws Exception {
        db_stmt.close();
        db_con.close();
    }
}
