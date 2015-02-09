package com.example.pranaykumar.viewsensorlist;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView)findViewById(R.id.textView); // reference to the text view

        SensorManager manager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder message = new StringBuilder();

        message.append(" Sensors on this device are: \n \n");

        for (Sensor sensor : sensors){
            message.append(" Name:"+ sensor.getName()+ "\n Type:"+sensorTypes.get(sensor.getType())+"\n \n");
           // message.append("Name:"+ sensor.getName()+ "\n Type:"+(sensor.getType())+"\n \n");
        }

        text.setText(message);

    }

    private HashMap<Integer, String> sensorTypes = new HashMap<Integer, String>();
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



}
