package com.ian.assignment4android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SensorEventListener {


    SensorManager manager;

    Sensor accelSensor;
    Sensor lightSensor;

    TextView accelInfoTextView;
    TextView lightInfoTextView;

    private HashMap<Integer, String> sensorTypes = new HashMap<>();
    {
        // Initialise the map of sensor type values and names
        sensorTypes.put(Sensor.TYPE_ACCELEROMETER, "TYPE_Accelerometer");
        sensorTypes.put(Sensor.TYPE_GYROSCOPE, "TYPE_Accelerometer");
        sensorTypes.put(Sensor.TYPE_LIGHT, "TYPE_LIGHT");
        sensorTypes.put(Sensor.TYPE_MAGNETIC_FIELD, "TYPE_MAGNETIC_FIELD");
        sensorTypes.put(Sensor.TYPE_RELATIVE_HUMIDITY, "TYPE_RELATIVE_HUMIDITY");
        sensorTypes.put(Sensor.TYPE_ORIENTATION, "TYPE_ORIENTATION");
        sensorTypes.put(Sensor.TYPE_TEMPERATURE, "TYPE_TEMPERATURE");
        sensorTypes.put(Sensor.TYPE_PRESSURE, "TYPE_PRESSURE");
        sensorTypes.put(Sensor.TYPE_PROXIMITY, "TYPE_PROXIMITY");
        sensorTypes.put(Sensor.TYPE_AMBIENT_TEMPERATURE, "TYPE_AMBIENT_TEMPERATURE");
        sensorTypes.put(Sensor.TYPE_GRAVITY, "TYPE_GRAVITY");
        sensorTypes.put(Sensor.TYPE_LINEAR_ACCELERATION, "TYPE_LINEAR_ACCELERATION");
        sensorTypes.put(Sensor.TYPE_ROTATION_VECTOR, "TYPE_ROTATION_VECTOR");
        sensorTypes.put(Sensor.TYPE_GAME_ROTATION_VECTOR, "TYPE_GAME_ROTATION_VECTOR");

    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Phone Parameters");
        setContentView(R.layout.activity_main);

        //Collect and display the date and time
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        TextView dateTextView = (TextView)findViewById(R.id.dateTextView);
        dateTextView.setText("Date and Time: " + currentDateTimeString);

        //Collect and display the os properties
        String s = "Debug Info:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";

        TextView osInfoTextView = (TextView)findViewById(R.id.osInfoTextView);
        osInfoTextView.setText(s);

        //Collect and display wifi data
        s = getCurrentSsid(getApplicationContext());

        TextView wifiInfoTextView = (TextView)findViewById(R.id.wifiInfoTextView);
        wifiInfoTextView.setText("WIFI Network Name: " + s);

        //Collect and display sensors available
        manager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder message = new StringBuilder();

        message.append(" Sensors on this device are: \n");

        for (Sensor sensor : sensors){
            message.append(" Name:"+ sensor.getName()+ "\n Type:"+ sensorTypes.get(sensor.getType())+"\n \n");
        }

        TextView sensorInfoTextView = (TextView)findViewById(R.id.sensorInfoTextView);
        sensorInfoTextView.setText(message);

        //Collect and display accelerometer data
        accelSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        accelInfoTextView = (TextView)findViewById(R.id.accelInfoTextView);

        //Collect and display light data
        lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);

        lightInfoTextView = (TextView)findViewById(R.id.lightInfoTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged (Sensor arg0, int arg1) {
        // Inflate the menu; this adds items to the action bar if it is present.

    }

    @Override
    public void onSensorChanged (SensorEvent event) {

        // first we need to check the event corresponds to accelerometer reading
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelInfoTextView.setText("Accelerometer Data:\nX: " + event.values[0]+ "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        }

        if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightInfoTextView.setText("\nLight Data:\n" + event.values[0]);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
