package com.example.pranaykumar.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    //we start by creating a sensor class
    SensorManager sm;
    // we select a particular sensor
    Sensor sensor;

    TextView displayReading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First, get a reference to the Sensor Manager
        sm =(SensorManager) getSystemService(SENSOR_SERVICE);

        // Then, Before selecting/referring a particular sensor for your application
        // determine whether that specific type of sensor exists on the device
        // by using the getDefaultSensor() method and passing in the type constant for a specific sensor


        if (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            // Success! There is ACCELEROMETER.
            // get reference to Accelerometer
            sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        }
        else{
            // failure! There's no ACCELEROMETER.
            finish();
        }

        //  use registerlistener in onResume method so that it saves power
       // sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        displayReading = (TextView)findViewById(R.id.display_reading);

    }


    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        if(event.sensor.getType()== Sensor.TYPE_ACCELEROMETER){

            displayReading.setText(" X: "+event.values[0]+"\n Y: " +event.values[1]+"\n Z: "+event.values[2]);

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
