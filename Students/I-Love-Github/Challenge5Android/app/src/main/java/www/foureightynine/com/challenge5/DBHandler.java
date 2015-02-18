package www.foureightynine.com.challenge5;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "FIRSTTABLE";
    private Context context;
    private ClientRunnable parent;
    private String sqlCommand;

//    private ArrayList<DataPoint> dataList;
    private long dataPointID;
    private long clientID;

    public DBHandler(Context context, ClientRunnable parent) {
        super(context, "client", null, 1);

        this.parent = parent;
        this.context = parent.getActivity().getBaseContext();

        initialize();
    }

    public void initialize() {

        SharedPreferences sp = parent.getActivity().getPreferences(Context.MODE_PRIVATE);
        clientID = sp.getLong("MyID", -1);
        dataPointID = sp.getLong("dataID", -1);

//        if (clientID == -1) {
            clientID = Math.abs(new HighQualityRandom().nextLong());
//        }

        if (dataPointID == -1) {
            dataPointID = 0;
        }

        parent.updateUI("Opened database successfully");
    }

    public boolean commitData(long time, String name, String data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TIME", time);
        values.put("NAME", name);
        values.put("DATA", data);
        values.put("SENT", 0);
        db.insert(TABLE_NAME, null, values);
        // db.close();
        return true;
    }

    public long getID() {

        return clientID;
    }

    public long getLatestTime() {

        long thisValue = 0;
        long returnValue = 0;

        SQLiteDatabase db = getWritableDatabase();
        sqlCommand = "SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC";
        Cursor c = db.rawQuery(sqlCommand, null);

        if (c.moveToFirst()) {
            do {
                thisValue = c.getLong(1);

                if (thisValue > returnValue) {
                    returnValue = thisValue;
                }
            } while (c.moveToNext());
        }
        c.close();
        // db.close();

        parent.updateUI("Latest entry timestamp: " + String.valueOf(returnValue));
        return returnValue;
    }

    public void markSent(int ackedID) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SENT", 1);

        db.update(TABLE_NAME, values, "ID =" + ackedID, null);
//        // db.close();
    }

    public Iterator<DataPoint> gatherDataForServer() {

        ArrayList<DataPoint> returnList = new ArrayList<DataPoint>();
        DataPoint thisPoint = null;

        SQLiteDatabase db = getWritableDatabase();
        sqlCommand = "SELECT * FROM " + TABLE_NAME + " WHERE SENT < 1";
//        sqlCommand = "SELECT * FROM " + TABLE_NAME + " WHERE TIME > " + String.valueOf(parent.getServerTS());
        Cursor c = db.rawQuery(sqlCommand, null);

        int key;
        long time;
        String name;
        String data;

        if (c.moveToFirst()) {
            do {

                key = c.getInt(0);
                time = c.getLong(1);
                name = c.getString(2);
                data= c.getString(3);

                thisPoint = new DataPoint(key, time, name, data);
                returnList.add(thisPoint);

            } while (c.moveToNext());
        }
        c.close();
        // db.close();

        Iterator<DataPoint> returnIterator = returnList.iterator();
        return returnIterator;

//        ArrayList<DataPoint> returnList = new ArrayList<DataPoint>();
//        DataPoint thisPoint;
//
//        synchronized (dataList) {
//            Iterator<DataPoint> iterator = dataList.iterator();
//
//            while (iterator.hasNext()) {
//                thisPoint = iterator.next();
//
//                if (thisPoint.time > parent.getServerTS()) {
//                    returnList.add(thisPoint);
//                }
//            }
//        }
//
//        return returnList.iterator();

    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        sqlCommand =
//                "CREATE TABLE ROUTYY " +
//                        "(ID INTEGER PRIMARY KEY, CLIENTID LONG, NUMTABLES INTEGER, TABLE1 VARCHAR);";
//        db.execSQL(sqlCommand);
//        parent.updateUI("Created ID table successfully");
//
//        long clientID = new HighQualityRandom().nextLong();
//
//        db = getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("ID", 1);
//        contentValues.put("CLIENTID", clientID);
//        contentValues.put("NUMTABLES", 1);
//        contentValues.put("TABLE1", "BASELINE");
//        db.insert("ROUTYY", null, contentValues);
//
//        sqlCommand = "CREATE TABLE BASELINE " +
//                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME LONG, NAME VARCHAR, DATA VARCHAR);";
//        db.execSQL(sqlCommand);
//        parent.updateUI("Created BASELINE table successfully");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS BASELINE");
//        onCreate(db);
//    }

    public void killDB() {
        SharedPreferences sp = parent.getActivity().getPreferences(Context.MODE_PRIVATE);
        sp.edit().putLong("MyID", clientID).commit();
        sp.edit().putLong("dataID", dataPointID).commit();

        parent.getCommHandler().lastSend();
    }

    public class DataPoint {
        public int id;
        public long time;
        public String name;
        public String data;

        public DataPoint(int dataPointID, long time, String name, String data) {
            this.id = dataPointID;
            this.time = time;
            this.name = name;
            this.data = data;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        sqlCommand =
//                "CREATE TABLE ROUTI " +
//                        "(ID INTEGER PRIMARY KEY, CLIENTID LONG, NUMTABLES INTEGER, TABLE1 TEXT);";
//        db.execSQL(sqlCommand);
//        parent.updateUI("Created ID table");
//
//
//        long clientID = new HighQualityRandom().nextLong();
//
//        db = getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("ID", 1);
//        contentValues.put("CLIENTID", clientID);
//        contentValues.put("NUMTABLES", 1);
//        contentValues.put("TABLE1", "BASELINE");
//        db.insert("ROUTYY", null, contentValues);
//        parent.updateUI("Created ROUTY entry");

        sqlCommand = "CREATE TABLE " + TABLE_NAME + " " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME LONG, NAME TEXT, DATA TEXT, SENT INTEGER);";
        db.execSQL(sqlCommand);
        parent.updateUI("Created BASELINE table successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS ROUTI");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}