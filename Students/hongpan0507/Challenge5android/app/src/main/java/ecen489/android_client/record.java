package ecen489.android_client;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hpan on 2/15/15.
 */
public class record implements Runnable {
    private client_activity parent;
    private JSONArray j_array = new JSONArray();
    private JSONObject complete_data;
    private String[] data = new String[6];
    public database db;
    private String table_name;
    private String[] column_name = {"LATITUDE", "LONGITUDE", "AZIMUTH", "PITCH", "ROLL", "WIFI_RSSI"};
    private String[] column_type = {"DOUBLE NULL", "DOUBLE NULL", "DOUBLE NULL", "DOUBLE NULL", "DOUBLE NULL", "DOUBLE NULL"};

    public record (client_activity parent_class,String db_name, String _table_name) {
        parent = parent_class;
        table_name = _table_name;
        parent.deleteDatabase(db_name);
        db = new database(parent, db_name, table_name,column_name, column_type);
    }

    public void run() {
        complete_data = parent.complete_data;
        for(int i = 0; i < data.length; ++i){
                data[i] = "test" + String.valueOf(i);
        }
        try {
            /*
            if (complete_data != null) {
                if (complete_data.has("Location")) {
                    j_array = (JSONArray) complete_data.get("Location");
                    if (j_array.length()!=0) {
                        data[0] = String.valueOf(j_array.get(0));
                        data[1] = String.valueOf(j_array.get(1));
                    } else {
                        data[0] = null;
                        data[1] = null;
                    }
                } else {
                    data[0] = null;
                    data[1] = null;
                }
                if (complete_data.has("Orientation")) {
                    j_array = (JSONArray) complete_data.get("Orientation");
                    if (j_array.length()!=0) {
                        data[2] = String.valueOf(j_array.get(0));
                        data[3] = String.valueOf(j_array.get(1));
                        data[4] = String.valueOf(j_array.get(2));
                    } else {
                        data[2] = null;
                        data[3] = null;
                        data[4] = null;
                    }
                } else {
                    data[2] = null;
                    data[3] = null;
                    data[4] = null;
                }
                if (complete_data.has("Wifi RSSI")) {
                    data[5] = complete_data.get("Wifi RSSI").toString();
                } else {
                    data[5] = null;
                }
            }
            */
            db.insert_val(table_name, data);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
