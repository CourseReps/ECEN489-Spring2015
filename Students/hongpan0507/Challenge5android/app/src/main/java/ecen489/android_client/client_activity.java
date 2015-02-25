package ecen489.android_client;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class client_activity extends ActionBarActivity implements SensorEventListener {
    private Socket sock;
    private int port_numb = 2015;
    private String server_ip = "127.0.0.1";
    public boolean new_data = false;
    public boolean update_com = false;
    public boolean send_com = false;
    private NumberFormat nf = NumberFormat.getInstance();
    private send send_thread;
    private Button butt_update;
    private Button butt_send;
    private ObjectOutputStream output_stream = null;

    private TextView TV_wifi;
    private int rssi = 0;
    private WifiManager wifi_man;
    private WifiInfo wifi;

    private TextView TV_latitude;
    private TextView TV_longitude;
    private double dev_loc_lat = 0;
    private double dev_loc_lon = 0;

    private TextView TV_sensor_type;
    private TextView TV_azimuth;
    private TextView TV_pitch;
    private TextView TV_roll;
    private SensorManager sensor_man;
    private Sensor orien_s;
    private List<Sensor> sensor_dev;

    public JSONObject complete_data = new JSONObject();
    private JSONArray temp_orien = new JSONArray();
    private JSONArray temp_loc = new JSONArray();

    public JSONObject send_complete_data = new JSONObject();
    private JSONArray send_temp_orien = new JSONArray();
    private JSONArray send_temp_loc = new JSONArray();

    private TextView TV_db;
    private database db;
    String[] column_name = {"LATITUDE", "LONGITUDE", "AZIMUTH", "PITCH", "ROLL", "WIFI_RSSI", "FLAG"};
    String[] column_type = {"DOUBLE", "DOUBLE", "DOUBLE", "DOUBLE", "DOUBLE", "DOUBLE", "INT"};
    private String[] data = new String[column_name.length];  //change data array size to fill in more elements
    private String table_name = "Asus_transformer";
    private String db_name = "opt_out5.db";
    //private record insert_data;
    private ArrayList array_list;
    private int row_ptr = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        nf.setMinimumIntegerDigits(3);
        nf.setMaximumFractionDigits(3);

        //---------------begin of set up client connection and get wifi data-------------------
        //retrieve parameters passed by main activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        port_numb = extras.getInt("port_numb");
        server_ip = extras.getString("ip_address");

        TV_wifi = (TextView) findViewById(R.id.TV_wifi);


        //---------------begin of database testing----------------------------
        TV_db = (TextView) findViewById(R.id.TV_db);
        //insert_data = new record(this, db_name, table_name);
        //new Thread(insert_data).start();

        /*
        String[] column_name = {"LATITUDE", "LONGITUDE", "AZIMUTH", "PITCH", "ROLL", "WIFI_RSSI"};
        String[] column_type = {"DOUBLE", "DOUBLE", "DOUBLE", "DOUBLE", "DOUBLE", "DOUBLE"};
        this.deleteDatabase(db_name);
        db = new database(this, db_name, table_name,column_name, column_type);
        for(int i = 0; i < data.length; ++i){
            data[i] = "test" + String.valueOf(i);
        }
        */

        /*
        String[] column_name = {"test"};
        String[] column_type = {"DOUBLE"};
        this.deleteDatabase(db_name);
        db = new database(this, db_name, table_name, column_name, column_type);
        data[0] = "123";
        db.insert_val(table_name, data);
        */

        this.deleteDatabase(db_name);
        db = new database(this, db_name, table_name,column_name, column_type);

        butt_update = (Button) findViewById(R.id.butt_update);
        butt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                update_com = true;
                db_data_insert();
            }
        });
        //---------------end of database testing----------------------------

        try {
            sock = new Socket(server_ip, port_numb);
            //output_stream = new ObjectOutputStream(sock.getOutputStream());
            //output_stream.flush();

            if (sock != null) {
                send_thread = new send(new ObjectOutputStream(sock.getOutputStream()),this);
                new Thread(send_thread).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        butt_send = (Button) findViewById(R.id.butt_send);
        butt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    String temp = "";
                    ArrayList array_2d = new ArrayList();

                    for (int i = 0; i < column_name.length; ++i) {   // column_name.length - 1 because we don't need the last column
                        array_list = db.get_val(i);
                        array_2d.add(array_list);
                    }

                    for (int i = 0; i < array_list.size(); ++i) {
                        /*
                        for (int j = 0; j < array_2d.size(); ++j) {
                            //temp = temp + " " + ((ArrayList) array_2d.get(j)).get(i);
                            if(j < 2) {
                                send_temp_loc.put(0, ((ArrayList) array_2d.get(j)).get(i));  //latitude
                                send_temp_loc.put(1, ((ArrayList) array_2d.get(j + 1)).get(i));  //longitude
                                send_complete_data.put("Location", temp_loc);
                            } else if (j < 5) {
                                send_temp_orien.put(0, ((ArrayList) array_2d.get(j)).get(i));  //latitude
                                send_temp_orien.put(1, ((ArrayList) array_2d.get(j + 1)).get(i));  //longitude
                                send_temp_orien.put(1, ((ArrayList) array_2d.get(j + 1)).get(i));  //longitude
                                send_complete_data.put("Location", temp_loc);
                            }
                        }*/

                        if(((ArrayList) array_2d.get(6)).get(i).toString().matches("0")) {
                            send_temp_loc.put(0, ((ArrayList) array_2d.get(0)).get(i));  //latitude
                            send_temp_loc.put(1, ((ArrayList) array_2d.get(1)).get(i));  //longitude
                            send_complete_data.put("Location", send_temp_loc);

                            send_temp_orien.put(0, ((ArrayList) array_2d.get(2)).get(i));  //Azimuth
                            send_temp_orien.put(1, ((ArrayList) array_2d.get(3)).get(i));  //Pitch
                            send_temp_orien.put(2, ((ArrayList) array_2d.get(4)).get(i));  //Roll
                            send_complete_data.put("Orientation", send_temp_orien);

                            send_complete_data.put("Wifi RSSI", ((ArrayList) array_2d.get(5)).get(i));
                            send_com = true;
                            db.update_flag(table_name, 6, i + 1, "1"); // add 1 because ID starts from 1
                        }
                        //output_stream.writeObject(send_complete_data.toString());
                        //output_stream.flush();
//select * from Asus_transformer;

                    }
                     TV_db.setText(send_complete_data.toString() + '\n' + complete_data.toString());
                    //TV_db.setText(((ArrayList) array_2d.get(6)).get(0).toString() + " " + test);
                    //TV_db.setText(send_complete_data.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //---------------end of set up client connection------------------------------

        //----------------begin of wifi----------------------------------------------
        wifi_man = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //----------------end of wifi----------------------------------------------

        //---------------begin of get orientation data------------------------------
        TV_sensor_type = (TextView) findViewById(R.id.TV_sensor_type);
        TV_azimuth = (TextView) findViewById(R.id.TV_azimuth);
        TV_pitch = (TextView) findViewById(R.id.TV_pitch);
        TV_roll = (TextView) findViewById(R.id.TV_roll);
        sensor_man = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_dev = sensor_man.getSensorList(Sensor.TYPE_ORIENTATION);
        for(int i=0; i<sensor_dev.size(); i++) {
            if (sensor_dev.get(i).getVendor().contains("Google Inc.")) {
                orien_s = sensor_dev.get(i);
                TV_sensor_type.setText(sensor_dev.get(i).toString());
            }
        }
        //---------------end of get orientation data------------------------------

        //---------------begin of get location data------------------------------
        TV_latitude = (TextView) findViewById(R.id.TV_latitude);
        TV_longitude = (TextView) findViewById(R.id.TV_longitude);

        //get reference from system location manager
        final LocationManager loc_man = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //listener that responds to a change of location
        LocationListener loc_lis = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TV_longitude.setText("Longitude = " + String.valueOf(location.getLongitude()));
                TV_latitude.setText("Latitude = " + String.valueOf(location.getLatitude()));
                dev_loc_lat = location.getLatitude();
                dev_loc_lon = location.getLongitude();

                try {
                    temp_loc.put(0,dev_loc_lat);
                    temp_loc.put(1,dev_loc_lon);
                    complete_data.put("Location", temp_loc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new_data = true;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //register listener to receive location updates
        loc_man.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc_lis);
        //---------------end of get location data------------------------------
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensor_man.registerListener(this, orien_s, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensor_man.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String azimuth_angle = nf.format(event.values[0]);
        String pitch_angle = nf.format(event.values[1]);
        String roll_angle = nf.format(event.values[2]);

        try {
            temp_orien.put(0, azimuth_angle);
            temp_orien.put(1, pitch_angle);
            temp_orien.put(2,roll_angle);
            complete_data.put("Orientation", temp_orien);
            TV_azimuth.setText("Azimuth = " + azimuth_angle);
            TV_pitch.setText("Pitch = " + pitch_angle);
            TV_roll.setText("Roll = " + roll_angle);

            wifi = wifi_man.getConnectionInfo();
            rssi = wifi.getRssi();
            complete_data.put("Wifi RSSI", rssi);
            TV_wifi.setText(String.valueOf(rssi));

            new_data = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void db_data_insert(){
        JSONArray j_array = new JSONArray();
        try {
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
            data[6] = "0";
            db.insert_val(table_name, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void db_data_pull(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_activity, menu);
        return true;
    }
}
