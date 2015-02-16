package ecen489.android_client;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.NumberFormat;
import java.util.List;

public class client_activity extends ActionBarActivity implements SensorEventListener {
    private Socket sock;
    private Thread client_thread;
    private int port_numb = 2015;
    private String server_ip = "10.202.44.28";
    private boolean new_data = false;
    private NumberFormat nf = NumberFormat.getInstance();

    private TextView TV_wifi;
    private int rssi = 0;

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

    private JSONObject complete_data = new JSONObject();
    private JSONArray temp_orien = new JSONArray();
    private JSONArray temp_loc = new JSONArray();

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

        try {
            sock = new Socket(server_ip, port_numb);
            if (sock != null) {
                send_thread send = new send_thread(sock);
                new Thread(send).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //---------------end of set up client connection------------------------------

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
            temp_orien.put(2, roll_angle);
            complete_data.put("Orientation", temp_orien);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TV_azimuth.setText("Azimuth = " + azimuth_angle);
        TV_pitch.setText("Pitch = " + pitch_angle);
        TV_roll.setText("Roll = " + roll_angle);

        TV_wifi.setText(String.valueOf(rssi));

        new_data = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_activity, menu);
        return true;
    }

    public class send_thread implements Runnable{
        private Socket client_socket;
        private ObjectOutputStream outgoing;

        //constructor
        public send_thread(Socket _client_sock){
            client_socket = _client_sock;
        }//end of constructor

        @Override
        public void run() {
            try {
                outgoing = new ObjectOutputStream(client_socket.getOutputStream());
                outgoing.flush();
                WifiManager wifi_man = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                while (!Thread.currentThread().isInterrupted()) {
                    if (new_data==true) {
                        try {
                            WifiInfo wifi = wifi_man.getConnectionInfo();
                            rssi = wifi.getRssi();
                            complete_data.put("Wifi RSSI in dBm = ", rssi);
                            outgoing.writeObject(complete_data.toString());
                            outgoing.flush();
                            new_data = false;
                            Thread.sleep(100);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }	//end of run
    }	//end of send_thread
}
