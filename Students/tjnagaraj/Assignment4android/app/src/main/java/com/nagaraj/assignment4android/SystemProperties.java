package com.nagaraj.assignment4android;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SystemProperties extends Activity implements SensorEventListener{

    SensorManager sensorManager;
    Sensor sensor;
    //protected  LocationManager locationManager;
    //protected LocationListener locationListener;
  // protected  Context context;
   //String provider;
   //Double latitude,longitude;
   // protected boolean gps_enabled,network_enabled;
    TextView displayDate;
    TextView displayOrientation;
    TextView displayLocation;
    TextView displayWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_properties);

     //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
     //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);



        displayOrientation = (TextView) findViewById(R.id.display_orientation);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        displayDate = (TextView) findViewById(R.id.display_date);
        displayLocation = (TextView) findViewById(R.id.display_location);
        displayWifi = (TextView) findViewById(R.id.display_wifi);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {

            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        } else {
            //No Sensor found.

            displayOrientation.setText("No ORIENTATION SENSOR found!");
        }
        Calendar c=Calendar.getInstance();
        SimpleDateFormat df =new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate =df.format(c.getTime());
        Toast.makeText(this,formattedDate,Toast.LENGTH_SHORT).show();
        displayDate.setText("Current date: "+formattedDate);
        getWifiProperties(getApplicationContext());
    }

    public void getWifiProperties(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo.isConnected()){
            final WifiManager wifiManager =(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.getConnectionInfo().getBSSID();

                displayWifi.setText("Wifi Deatails\n  SSID: "+wifiManager.getConnectionInfo().getSSID()+"\n Network Id: " +wifiManager.getConnectionInfo().getNetworkId()+ "\n  IP Address: "+wifiManager.getConnectionInfo().getIpAddress());

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        //locationManager.removeUpdates(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_system_properties, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
            displayOrientation.setText("Orientation:\n  X: "+event.values[0]+"\n  Y: " +event.values[1]+"\n  Z: "+ event.values[2]);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
/*
    @Override
    public void onLocationChanged(Location location) {
       latitude=location.getLatitude();
       longitude=location.getLongitude();
       displayLocation.setText("Location:\n  Latitude:"+latitude+"  Longitude"+longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Location", "status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Location", "enable");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Location","disable");
    }
*/
}

