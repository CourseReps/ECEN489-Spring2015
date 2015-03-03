package r2data4;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbcon extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="MyDb";

    public Dbcon(Context context) {

        super(context, DATABASE_NAME, null, 1);


    }

    @Override

    public void onCreate(SQLiteDatabase database) {

//        database.execSQL("CREATE TABLE datatracker (_id INTEGER PRIMARY KEY AUTOINCREMENT, bssid TEXT, rssi INTEGER, location TEXT, flag TEXT);");
        database.execSQL("CREATE TABLE timetracker (boxid TEXT NOT NULL PRIMARY KEY, lastPbTime TEXT DEFAULT 0, lastSVRTime TEXT DEFAULT 0);");
        ContentValues contentValues = new ContentValues(1);
        contentValues.put("boxid","PB1");
        database.insert("timetracker",null,contentValues);
        contentValues.put("boxid","PB2");
        database.insert("timetracker",null,contentValues);
        contentValues.put("boxid","PB3");
        database.insert("timetracker",null,contentValues);
        contentValues.put("boxid","PB4");
        database.insert("timetracker",null,contentValues);
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS timetracker");

        onCreate(db);

    }


    public String getPbTime(String boxid) {

        String where = "boxid='"+boxid+"'";
        Cursor cursor = getReadableDatabase().query(true,"timetracker",new String[] { "lastPbTime"},where, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("lastPbTime"));

    }

    public String getSvrTime(String boxid) {

        String where = "boxid='"+boxid+"'";
        Cursor cursor = getReadableDatabase().query(true,"timetracker",new String[] { "lastSVRTime"},where, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("lastSVRTime"));

    }


    public void updatePbTime(String boxid, String pbtime ){
        getWritableDatabase().execSQL("update timetracker set lastPbTime='"+pbtime+"' where boxid='"+boxid+"';");
    }

    public void updateSvrTime(String boxid, String svrtime ){
        getWritableDatabase().execSQL("update timetracker set lastSVRTime='"+svrtime+"' where boxid='"+boxid+"';");
    }
}