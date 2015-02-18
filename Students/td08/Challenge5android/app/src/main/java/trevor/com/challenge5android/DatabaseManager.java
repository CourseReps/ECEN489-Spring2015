package trevor.com.challenge5android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
Class used to interface with local SQL database. Contains methods to insert, read, and retrieve entries
in a SQL formatted command syntax
*/

public class DatabaseManager extends SQLiteOpenHelper {

    //static class variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Value";
    private static final String LOCAL_TABLE_NAME = "DATA";
    private static final String SERVER_TABLE_NAME = "DATA";
    static int entries;  //static int used to track number of entries made to table during sense

    //instance variables
    private SQLiteDatabase db;
    private MainActivity parent;

    //constructor to create sql database
    public DatabaseManager(Context context, MainActivity parent) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.parent = parent;
        // TODO Auto-generated constructor stub
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        String createTable = "CREATE TABLE " + LOCAL_TABLE_NAME + " (ID INTEGER PRIMARY KEY, LAT DOUBLE, LONG DOUBLE)";
        db.execSQL(createTable);
        entries = 0; //initialize entries counter to zero
        Log.d("DatabaseManager", "created table during onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + LOCAL_TABLE_NAME);
        onCreate(db);
    }

    //method to create table using name passed to method
    public void createTable (SQLiteDatabase db, String table) {
        this.db = db;
        String createTable = "CREATE TABLE " + table + " (ID INTEGER PRIMARY KEY, LAT DOUBLE, LONG DOUBLE)";
        db.execSQL(createTable);
        Log.d("DatabaseManager", "created table: " + table + " from createTable");
    }

    //enter gps coordinates into table
    public void addPoint(double lat, double ltude) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("LAT", lat);
        values.put("LONG", ltude);
        db.insert(LOCAL_TABLE_NAME, null, values);
        db.close(); // Closing database connection
        entries++;  //increment entries counter after an entry is written to table
    }

    //method to read specific row from table
    public String readRow (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(LOCAL_TABLE_NAME, new String[] {"ID", "LAT", "LONG"}, "ID = ?", new String[] {String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String row = "Row entry: ID = " + cursor.getInt(0) + ", LAT = " + cursor.getDouble(1) + ", LONG = " + cursor.getDouble(2);
        return row;
    }

    //method that returns a row in SQL command format to be entered into server database table
    public String getData (int idNum) {
        int id = idNum == 0 ? 1 : idNum;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(LOCAL_TABLE_NAME, new String[] {"ID", "LAT", "LONG"}, "ID = ?", new String[] {String.valueOf(id)}, null, null, null, null);
        String sql = null;
        if (cursor != null) {
            cursor.moveToFirst();
            sql = "INSERT INTO " + SERVER_TABLE_NAME + " (ID, LAT, LONG) VALUES (" + cursor.getInt(0) + ", " + cursor.getDouble(1) + ", " + cursor.getDouble(2) + ")";
        }
        else sql = null;
        return sql;
    }

    //method used to clear table entries
    public void clearTable () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOCAL_TABLE_NAME, null, null);
        db.close();
        CollectData.count = 0; //reset data point counter
        ConnectToServer.LastID = 0; //reset LastID
    }

    //method that checks existence of table and returns boolean. Will also create table if necessary
    public boolean tableExists() {
        boolean tableExists = false;
        try {
            String createTable = "CREATE TABLE " + LOCAL_TABLE_NAME + " (ID INTEGER PRIMARY KEY, LAT DOUBLE, LONG DOUBLE)";
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
}