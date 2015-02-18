package com.nagaraj.challenge5android;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.cert.X509Certificate;

/**
 * Created by NAGARAJ on 2/16/2015.
 */
public class DatabaseManager {
    // Setting TAG for logging purpose
    private static final String TAG = "DatabaseManager";

    //Database fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0 ;

    public static final String KEY_X = "X";
    public static final int COL_X =1 ;
    public static final String KEY_Y = "Y";
    public static final int COL_Y = 2 ;
    public static final String KEY_Z = "Z";
    public static final int COL_Z = 3 ;
    public static final String KEY_DATE_TIME = "DATE_TIME";
    public static final int COL_DATE_TIME = 4 ;
    public static final String KEY_FLAG = "FLAG";
    public static final int COL_FLAG = 5 ;

    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_X, KEY_Y, KEY_X, KEY_DATE_TIME, KEY_FLAG};

    public static final String DATABASE_NAME = "LocalDatabase";
    public static final String DATABASE_TABLE = "OrientationSensor";


    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_X + " double, "
                    + KEY_Y + " double, "
                    + KEY_Z + " double, "
                    + KEY_DATE_TIME + " string not null,"
                    + KEY_FLAG + " String not null"
                    + ");";

    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DatabaseManager(Context context) {
        this.context = context;
        myDBHelper = new DatabaseHelper(context);
    }

    public DatabaseManager open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    public long insertRow(double x, double y, double z, String date_time, String flag) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_X, x);
        initialValues.put(KEY_Y, y);
        initialValues.put(KEY_Z, z);
        initialValues.put(KEY_DATE_TIME, date_time);
        initialValues.put(KEY_FLAG, flag);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getFlagNo() {
        String where = KEY_FLAG + "= \"No\"";
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }



    public boolean updateRow(long rowId, double x, double y, double z, String date_time, String flag) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_X, x);
        newValues.put(KEY_Y, y);
        newValues.put(KEY_Z, z);
        newValues.put(KEY_DATE_TIME, date_time);
        newValues.put(KEY_FLAG, flag);
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

        private static class DatabaseHelper extends SQLiteOpenHelper {
            DatabaseHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase _db) {
                _db.execSQL(DATABASE_CREATE_SQL);
            }

            @Override
            public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
                Log.w(TAG, "Upgrading application's database from version " + oldVersion
                        + " to " + newVersion + ", which will destroy all old data!");

                // Destroy old database:
                _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

                // Recreate new database:
                onCreate(_db);
            }
        }
    }

