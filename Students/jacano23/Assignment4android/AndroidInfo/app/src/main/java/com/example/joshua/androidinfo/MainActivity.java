package com.example.joshua.androidinfo;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(        R.layout.activity_main);
        TextView dateView = (TextView) findViewById(R.id.dateview);
        // Date
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        dateView.setText("Date and Time: " + currentDateTimeString);

        //OS Information of Device
        String operatingInfo = "Android OS Information: \n";
        operatingInfo += "OS Architecture: " + System.getProperty("os.arch") + "\n";
        operatingInfo += "OS Name: " + System.getProperty("os.name") + "\n";
        operatingInfo += "OS Version: " + System.getProperty("os.version") + "\n";

        TextView osTextView = (TextView)findViewById(R.id.osview);
        osTextView.setText(operatingInfo);

        // Wifi Information
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String info = "Wifi-Info: ";
        info += "SSID" + wifiInfo.getSSID();

        TextView wifiTextView = (TextView)findViewById(R.id.wifiview);
        wifiTextView.setText(info);

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
