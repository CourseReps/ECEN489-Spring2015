package com.kcwmasterpiece.databasesender;

/**
 * Created by kwilk_000 on 2/22/2015.
 */

import java.io.File;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    private static String DATABASE_NAME = "IPAddressInfo.db";
    static String DB_PATH;
    private final Context myContext;

    // table name
    private static final String TABLE_NAME = "Values";

    static int entries;
    private SQLiteDatabase db;

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        myContext = context;
        DB_PATH = myContext.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        // TODO Auto-generated constructor stub
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;
        String createTable;

//        CREATE_CONTACTS_TABLE = "create table " + TABLE_Languages + "("
//                + KEY_ID + " int primary key autoincrement, " + KEY_NAME
//                + " text not null);";

        createTable = "CREATE TABLE DATA (ID INTEGER PRIMARY KEY, LAT DOUBLE, LONG DOUBLE)";

        db.execSQL(createTable);
        entries = 0;
        Log.d("Database", "created table from onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void add(double lat, double ltude) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, lang.getValue()); // Contact Name
        //values.put("DATE", date);
        values.put("LAT", lat);
        values.put("LONG", ltude);
        // Inserting Row
        //db.insert(TABLE_Languages, null, values);
        db.insert("DATA", null, values);

        db.close(); // Closing database connection
        entries++;
    }

    public String readDB (){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("DATA", new String[] {"DATE"}, null, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        String date = cursor.getString(0);
        return date;
    }

    public String getData (int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("DATA", new String[] {"ID", "LAT", "LONG"}, "ID = ?", new String[] {String.valueOf(id)}, null, null, null, null);


        String sql = null;
        if (cursor != null) {
            cursor.moveToFirst();
            sql = "INSERT INTO DATA (ID, LAT, LONG) VALUES (" + cursor.getInt(0) + ", " + cursor.getDouble(1) + ", " + cursor.getDouble(2) + ")";
        }
        else sql = null;

        return sql;
    }

    public boolean tableExists() {
        boolean tableExists = false;
        try {
            String createTable = "CREATE TABLE DATA (ID INTEGER PRIMARY KEY, LAT DOUBLE, LONG DOUBLE)";

            //createTable = "CREATE TABLE DATA (ID INT PRIMARY KEY AUTO_INCREMENT, DATE TEXT, LONG DOUBLE, LAT DOUBLE)";

            db.execSQL(createTable);
            Log.d("Database", "Created table from tableExists method");
            entries = 0;
        }
        catch (SQLiteException e) {
            tableExists = true;
            Log.d("Database", "TABLE ALREADY EXISTS");
        }
        finally {
            return tableExists;
        }
    }


    // Updating single record
//    public int update(Values value) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        //values.put(KEY_NAME, value.getValue());
//
//        // updating row
//        //return db.update(TABLE_NAME, values, KEY_ID + " = ?",
//                //new String[] { String.valueOf(value.getId()) });
//
//    }
}
