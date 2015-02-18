package com.example.tungala.challenge5android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tungala on 2/15/2015.
 */
public class DbCon extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="MyDb";

    public DbCon(Context context) {

        super(context, DATABASE_NAME, null, 1);


    }

    @Override

    public void onCreate(SQLiteDatabase database) {

        database.execSQL("CREATE TABLE client (_id INTEGER PRIMARY KEY AUTOINCREMENT, bssid TEXT, rssi INTEGER, location TEXT, flag TEXT);");

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS client");

        onCreate(db);

    }

    public Long addRecord(String location, String bssid, Integer rssi, String flag){
        ContentValues contentValues = new ContentValues(4);
        contentValues.put("bssid",bssid);
        contentValues.put("rssi",rssi);
        contentValues.put("location",location);
        contentValues.put("flag",flag);

        return getWritableDatabase().insert("client",null,contentValues);
    }

    public Cursor getUnsentRecords() {
        String where = "flag='No'";
        Cursor cursor = getReadableDatabase().query(true,"client",new String[] { "_id", "location", "bssid", "rssi"},where, null, null, null, null, null);
        return cursor;
    }

    public Cursor getInsertedRecords() {
        String query = "select * from client";
        Cursor cursor = getWritableDatabase().rawQuery(query,null);
        return cursor;
    }

    public void deleteAll(){
        getWritableDatabase().delete("client",null,null);
        getWritableDatabase().execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME ='client'");
    }

    public void updateTable(Long id){
        getWritableDatabase().execSQL("update client set flag='Yes' where _id="+id+";");
    }

}
