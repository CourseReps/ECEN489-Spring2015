package com.mandel.paramapp;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Hashtable;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.DatabaseUtils;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.database.sqlite.SQLiteDatabase;
        import android.util.Log;

        import org.json.JSONArray;
        import org.json.JSONObject;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDB.db";
    public static final String PARAMETERS_TABLE_NAME = "parameters";
    public static final String PARAMETERS_COLUMN_ID = "id";
    public static final String PARAMETERS_COLUMN_TIME = "time";
    public static final String PARAMETERS_COLUMN_MACADDRESS = "macaddress";
    public static final String PARAMETERS_COLUMN_FLAG= "flag";
    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table parameters " +
                        "(id integer primary key, time text, macaddress text, flag int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS parameters");
        onCreate(db);
    }

    public boolean insertParameter  (String time, String macAddress, int flag)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("time", time);
        contentValues.put("macaddress", macAddress);
        contentValues.put("flag", flag);

        db.insert("parameters", null, contentValues);
        return true;
    }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from parameters where id="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PARAMETERS_TABLE_NAME);
        return numRows;
    }
    public boolean updateParameter (Integer id, String time, String macAddress, int flag)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", time);
        contentValues.put("macaddress", macAddress);
        contentValues.put("flag", flag);

        db.update("parameters", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("parameters",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList getAllMacAddress()
    {
        ArrayList array_list = new ArrayList();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from parameters", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PARAMETERS_COLUMN_MACADDRESS)));
            res.moveToNext();
        }
        return array_list;
    }

    public JSONArray toJson(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from parameters where(flag != 1)", null );

        JSONArray resultSet     = new JSONArray();

        res.moveToFirst();
        while (res.isAfterLast() == false) {

            int totalColumn = res.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (res.getColumnName(i) != null && !res.getColumnName(i).equals("flag")) {
                    try {
                        if (res.getString(i) != null) {
                            Log.d("TAG_NAME", res.getString(i));
                            rowObject.put(res.getColumnName(i), res.getString(i));
                        } else {
                            rowObject.put(res.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            res.moveToNext();
        }
        res.close();
        Log.d("TAG_NAME", resultSet.toString() );
        return resultSet;
    }

   public void updateToSent(){
       SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("update parameters set flag = 1 where flag != 1");
   }

   public String timeStringBuilder(Calendar calendar)
   {
        String date;
        String time;
       date =""+calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
       time = ""+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
       return date + " " + time;
    }
}