package com.example.rhoadswylde.systemproperties;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class WifiScanReceiver extends BroadcastReceiver {
    public void onReceive(Context c, Intent intent) {
    }
}
public class SystemProperties extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_properties);

        //Date
        Date dateDisplay = new Date();
        DateFormat df = new SimpleDateFormat("yyyy/MMM/dd");
        String date = df.format(dateDisplay);
        //Time
        Date timeDisplay = new Date();
        DateFormat tf = new SimpleDateFormat("HH:mm:ss");
        String time = tf.format(timeDisplay);


        //Get the location of the device using LocationManager and .getSystemService
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        double lat = location.getLatitude();
        double longitude = location.getLongitude();

        /*This will get*/
        BluetoothManager BTmanager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter BT_Adapter = BTmanager.getAdapter();


        /*Display the device information*/
            TextView textView = new TextView(this);
            textView.setTextSize(12);
            textView.setText("Date: " + date + "\nTime: " + time + "\nLatitude: " +lat+
                    "\nLongitude: " +longitude);

            setContentView(textView);
            textView.setMovementMethod(new ScrollingMovementMethod());


        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_system_properties, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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

