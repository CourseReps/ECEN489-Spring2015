package www.foureightynine.com.challenge4;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;


// PLACEHOLDER beause I fail at android SQL
public class DBHandler {
//public class DBHandler extends SQLiteOpenHelper {

//    private Context context;
    private ClientRunnable parent;
//    SQLiteDatabase db;
//    private Cursor c;
//    private String sqlCommand;

    private ArrayList<DataPoint> dataList;
    private int dataPointID;
    private long clientID;

    public DBHandler(ClientRunnable parent) {
//    public DBHandler(Context context, ClientRunnable parent) {
//        super(context, "client", null, 1);

        this.parent = parent;
//        this.context = parent.getActivity().getBaseContext();

        initialize();
    }

    public void initialize() {

        SharedPreferences sp = parent.getActivity().getPreferences(Context.MODE_PRIVATE);
        clientID = sp.getLong("MyID", -1);
        dataPointID = sp.getInt("dataID", -1);

        if (clientID == -1) {
            clientID = Math.abs(new HighQualityRandom().nextLong());
        }

        if (dataPointID == -1) {
            dataPointID = 0;
        }

        dataList = new ArrayList<DataPoint>();
//        c = null;
//        sqlCommand = null;
//
//        try {
//
//            db = parent.getActivity().openOrCreateDatabase("client", Context.MODE_PRIVATE, null);
//
//            c = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'ROUTYY'", null);
//
//            sqlCommand =
//                    "CREATE TABLE IF NOT EXISTS ROUTYY " +
//                            "(ID INTEGER PRIMARY KEY, CLIENTID LONG, NUMTABLES INTEGER, TABLE1 VARCHAR);";
//            db.execSQL(sqlCommand);
//            parent.updateUI("Created ID table successfully");
//
//        } catch (Exception e) {
//            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
//        }
//
//
//            long clientID = new HighQualityRandom().nextLong();
//
//
//        try {
//            db = getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("ID", 1);
//            contentValues.put("CLIENTID", clientID);
//            contentValues.put("NUMTABLES", 1);
//            contentValues.put("TABLE1", "BASELINE");
//            db.insert("ROUTYY", null, contentValues);
//            parent.updateUI("Created ROUTY entry");
//
//        } catch (Exception e) {
//            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
//        }
//
//        try {
//
//            sqlCommand = "CREATE TABLE IF NOT EXISTS BASELINE " +
//                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME LONG, NAME VARCHAR, DATA VARCHAR);";
//            db.execSQL(sqlCommand);
//            parent.updateUI("Created BASELINE table successfully");
//
//
//        } catch (Exception e) {
//            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
//        }
//
        parent.updateUI("Opened database successfully");
    }

    public boolean commitData(long time, String name, String data) {

        DataPoint newPoint = new DataPoint(dataPointID, time, name, data);
        dataList.add(newPoint);
        dataPointID++;
        return true;
//        try {
//
//            db = getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("TIME", time);
//            contentValues.put("NAME", name);
//            contentValues.put("DATA", data);
//            db.insert("BASELINE", null, contentValues);
//
//        } catch (Exception e) {
//            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
//            return false;
//
//        } finally {
////            parent.updateUI("Write to DB successful");
//            return true;
//        }
    }

    public long getID() {

        return clientID;

//        long returnValue = 0;
//
//        try {
//            db = getReadableDatabase();
//            sqlCommand = "SELECT * FROM ROUTYY WHERE ID = 1;";
//            c = db.rawQuery(sqlCommand, null);
//            returnValue = c.getLong(c.getColumnIndex("CLIENTID"));
//
//        } catch (Exception e) {
//            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
//            return 0;
//
//        } finally {
//            parent.updateUI("This client ID is: " + String.valueOf(returnValue));
//            return returnValue;
//        }
    }

    public long getLatestTime() {

        long returnTime = 0;
        long thisTime;

        synchronized (dataList) {
            Iterator<DataPoint> iterator = dataList.iterator();

            while (iterator.hasNext()) {

                thisTime = iterator.next().time;

                if (thisTime > returnTime) {
                    returnTime = thisTime;
                }
            }
        }

        return returnTime;

//        long returnValue = 0;
//
//        try {
//            db = getReadableDatabase();
//            sqlCommand = "SELECT * FROM BASELINE ORDER BY ID DESC";
//            c = db.rawQuery(sqlCommand, null);
//            returnValue = c.getLong(c.getColumnIndex("TIME"));
//
//        } catch (Exception e) {
//            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());            return 0;
//
//        } finally {
//            parent.updateUI("Latest entry timestamp: " + String.valueOf(returnValue));
//            return returnValue;
//        }
    }

    public Iterator<DataPoint> gatherDataForServer() {

        ArrayList<DataPoint> returnList = new ArrayList<DataPoint>();
        DataPoint thisPoint;

        synchronized (dataList) {
            Iterator<DataPoint> iterator = dataList.iterator();

            while (iterator.hasNext()) {
                thisPoint = iterator.next();

                if (thisPoint.time > parent.getServerTS()) {
                    returnList.add(thisPoint);
                }
            }
        }

        return returnList.iterator();

//        try {
//            db = getReadableDatabase();
//            sqlCommand = "SELECT * FROM BASELINE WHERE TIME > " + String.valueOf(parent.getServerTS());
//            c = db.rawQuery(sqlCommand, null);
//
//        } catch (Exception e) {
//            parent.updateUI(e.getClass().getName() + ": " + e.getMessage());
//            return null;
//
//        } finally {
//            parent.updateUI("Sending entries to server...");
//            return c;
//        }
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
}