package com.example.pranaykumar.mobileplatform;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SensorEventListener {
//public class MainActivity extends ActionBarActivity  {
    //we start by creating a sensor class
    SensorManager sm;
    // we select a particular sensor
    Sensor orientationSensor;


    // textView is the TextView view that should display it
    TextView displayDate = (TextView)findViewById(R.id.display_date);
    TextView displaySensor = (TextView)findViewById(R.id.display_sensor);
    TextView displayOrientation = (TextView)findViewById(R.id.display_orientation);
    TextView displayLocation = (TextView)findViewById(R.id.display_location);
    TextView displayWifi = (TextView)findViewById(R.id.display_wifi);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Displaying time and date
        //StringBuilder message1 = new StringBuilder();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        //message1.append(" DATE & TIME: \n " + currentDateTimeString  +"\n \n \n" );
        displayDate.setText(" DATE & TIME: \n " + currentDateTimeString  +"\n \n \n");



        // First, get a reference to the Sensor Manager
        sm =(SensorManager) getSystemService(SENSOR_SERVICE);

        // Then, Before selecting/referring a particular sensor for your application
        // determine whether that specific type of sensor exists on the device
        // by using the getDefaultSensor() method and passing in the type constant for a specific sensor
       // List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
        //StringBuilder message2 = new StringBuilder();
        //message2.append(" Sensors on this Device are: \n \n");
       // for (Sensor sensorList : sensors){
        //    message2.append(" Name:"+ sensorList.getName()+ "\n \n");
            // message.append("Name:"+ sensor.getName()+ "\n Type:"+(sensor.getType())+"\n \n");
        //}
       // displaySensor.setText(message2);

        orientationSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        // Register the listener with the Location Manager to receive location updates
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);

        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=null;
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        displayWifi.setText("Wifi info \n" + networkInfo+"\n\n");

    }


    class myLocationListener implements LocationListener{


        @Override
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.

            if (location != null){
                double plong = location.getLongitude();
                double plat = location.getLatitude();

                displayLocation.setText("Latitude: \n"+ Double.toString(plat)+"\n Longitude"+Double.toString(plong)+"\n\n");


            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}


        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

    }







    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged (Sensor arg0, int arg1) {
        // Inflate the menu; this adds items to the action bar if it is present.

    }

    @Override
    public void onSensorChanged (SensorEvent event) {

        // first we need to check the event corresponds to accelerometer reading
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            displayOrientation.setText("Accelerometer Readings : \n\n" + " X: " + event.values[0] + "\n Y: " + event.values[1] + "\n Z: " + event.values[2] + "\n \n \n");
            // displayReading.setText(" X: "+event.values[0]+"\n Y: " +event.values[1]+"\n Z: "+event.values[2]);

        }
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


/*
<TextView
android:layout_width="wrap_content"
        android:layout_height="wrap_content"

        android:id="@+id/display_date" />

<TextView
android:layout_width="wrap_content"
        android:layout_height="wrap_content"

        android:id="@+id/display_sensor" />

<TextView
android:layout_width="wrap_content"
        android:layout_height="wrap_content"

        android:id="@+id/display_orientation" />

<TextView
android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/display_location" />

<TextView
android:layout_width="wrap_content"
        android:layout_height="wrap_content"

        android:id="@+id/display_wifi" /> */
