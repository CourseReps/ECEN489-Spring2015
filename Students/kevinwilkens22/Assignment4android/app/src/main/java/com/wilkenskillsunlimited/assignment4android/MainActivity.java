package com.wilkenskillsunlimited.assignment4android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.*;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.net.wifi.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


class WifiScanReceiver extends BroadcastReceiver {
    public void onReceive(Context c, Intent intent){

    }
}

public class MainActivity extends ActionBarActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date dateDisplay = new Date();
        DateFormat df = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
        String date = df.format(dateDisplay);


// Use LocationManager to get location data:
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
       // Location latlong = new Location(locationManager.NETWORK_PROVIDER);
        // Use Network location data:
        Location loc1 = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        double lat = loc1.getLatitude();
        double longitude = loc1.getLongitude();

        //Wifi Information
        WifiManager mainWifiObj;
        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiScanReceiver wifiReceiver = new WifiScanReceiver();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        List<WifiConfiguration> wificonfig = mainWifiObj.getConfiguredNetworks();


        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(date + "\n" + System.getProperty("os.version")
                        + "\n" + System.getProperty("os.arch")
                        + "\nLatitude: " + lat
                        + "\nLongitude: " + longitude
                        + "\nWifi Info: \n" + wificonfig

        );
        textView.setMovementMethod(new ScrollingMovementMethod());
        setContentView(textView);
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

