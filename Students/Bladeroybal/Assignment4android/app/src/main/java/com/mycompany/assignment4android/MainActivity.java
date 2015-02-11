package com.mycompany.assignment4android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Date and Time on Device
        Date datetime = new Date();
        String time = "It is currently  " + datetime;

        //OS Information of Device
        String operatingInfo = "Android OS Information: \n";
        operatingInfo += "OS Architecture: " + System.getProperty("os.arch") + "\n";
        operatingInfo += "OS Name: " + System.getProperty("os.name") + "\n";
        operatingInfo += "OS Version: " + System.getProperty("os.version") + "\n";
        operatingInfo += "OS API: " + Build.VERSION.SDK_INT;

        //Device Information
        String deviceInfo = "Android Device Information: \n";
        deviceInfo += "Device Manufacturer: " + Build.MANUFACTURER + "\n";
        deviceInfo += "Device Brand: " + Build.BRAND + "\n";
        deviceInfo += "Device Product Name: " + Build.PRODUCT + "\n";
        deviceInfo += "Device Model: " + Build.MODEL;

        //WIFI Information
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String wirelessInfo = "WIFI Information: \n";
        if (!manager.isWifiEnabled()) {
            wirelessInfo += "Your device is not connected to WIFI";
        } else {
            wirelessInfo += "Device Mac Address: " + info.getMacAddress() + "\n";
            wirelessInfo += "Connected To: " + info.getSSID() + "\n";
            wirelessInfo += "Signal Strength (in dBm): " + info.getRssi() + "\n";
            wirelessInfo += "Connection Speed (in Mbps): " + info.getLinkSpeed();
        }

        //Location Information
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String coordinates = "Location Information: \n";
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            coordinates += "Your last known coordinates are \n";
            coordinates += "Latitude: " + location.getLatitude() + "\n";
            coordinates += "Longitude " + location.getLongitude() + "\n";
        }
        else {
            coordinates += "Your GPS is not enabled.";
        }
        //Text on Android Screen
        TextView dateTextView = (TextView)findViewById(R.id.dateTextView);
        dateTextView.setText(time);

        TextView osTextView = (TextView)findViewById(R.id.osTextView);
        osTextView.setText(operatingInfo);

        TextView deviceTextView = (TextView)findViewById(R.id.deviceTextView);
        deviceTextView.setText(deviceInfo);

        TextView wifiTextView = (TextView)findViewById(R.id.wifiTextView);
        wifiTextView.setText(wirelessInfo);

        TextView locationTextView = (TextView)findViewById(R.id.locationTextView);
        locationTextView.setText(coordinates);





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
