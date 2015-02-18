package com.mandel.paramapp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo currentWifi;
        String macAddress;

        mydb = new DBHelper(this);
        Calendar calendar;
        for(int i =0; i<5; i++) {
            currentWifi = mWifi.getConnectionInfo();
            if (currentWifi != null && currentWifi.getMacAddress() != null) {
                macAddress = currentWifi.getMacAddress();
            } else {
                macAddress = "No Connection";
            }

            calendar = Calendar.getInstance();
            String time = mydb.timeStringBuilder(calendar);
            mydb.insertParameter(time, macAddress, 0);

        }
        System.out.println("Connecting to Server");
        Intent mServiceIntent = new Intent(this, ServerPushService.class);
        this.startService(mServiceIntent);

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
