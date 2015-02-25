package ecen489.android_client;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hpan on 2/14/15.
 */

public class database extends SQLiteOpenHelper {
    private String table_name;
    private String[] column_name;
    private String[] column_type;
    private String sql;
    private SQLiteDatabase db;
    private ContentValues CV = new ContentValues();
    private Cursor db_ptr;

    //constructor
    public database(Context context, String db_name, String table_name, String[] column_name, String[] column_type){
        super(context, db_name, null, 1); //default constructor; must be called
        this.table_name = table_name;
        this.column_name = column_name;
        this.column_type = column_type;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sql = "Create Table If Not Exists " + table_name + " " +
              "(ID INTEGER PRIMARY KEY AUTOINCREMENT";
        for (int i = 0; i < column_name.length; ++i) {
            sql = sql + ", " + column_name[i] + " " + column_type[i];
        }
        sql = sql + ");";
        db.execSQL(sql);
    }

    public void insert_val(String _table_name, String[] data) {
        this.db = this.getWritableDatabase();
        for (int i = 0; i < data.length ; i++) {
            CV.put(column_name[i],data[i]);
        }
        this.db.insert(_table_name, null, CV);
    }

    public void update_flag(String _table_name, int col_index, int row_id, String bool) {
        this.db = this.getWritableDatabase();
        CV.put(column_name[col_index],bool);
        this.db.update(_table_name, CV, "id = ? ", new String[] {Integer.toString(row_id)});
    }

    public ArrayList get_val(int col_index) {
        ArrayList array_list = new ArrayList();
        this.db = this.getReadableDatabase();
        db_ptr = db.rawQuery("select * from " + table_name, null);
        db_ptr.moveToFirst();
        while (db_ptr.isAfterLast() == false) {
            array_list.add(db_ptr.getString(db_ptr.getColumnIndex(column_name[col_index])));
            db_ptr.moveToNext();
        }
        db_ptr.close();
        return array_list;
    }

    public void sql_comm (String _sql){
        sql = _sql;
        this.db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
