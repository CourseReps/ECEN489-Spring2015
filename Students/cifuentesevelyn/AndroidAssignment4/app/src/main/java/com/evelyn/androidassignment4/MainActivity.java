package com.evelyn.androidassignment4;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    //Wi-Fi Services
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
        setContentView(R.layout.activity_main);

        //Getting Wifi Info from outside OnCreate
        String wifi = getCurrentSsid(getApplicationContext());

        //For Date and Time
        Calendar cal = Calendar.getInstance();
        System.out.println("Current time =>" +cal.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyy HH:mm:ss");
        String DateTime = df.format(cal.getTime());

        Toast.makeText(this,DateTime, Toast.LENGTH_SHORT).show();

        //Device and Operating System Information
        String Device = " My Device:\t" + android.os.Build.DEVICE + "\n\n" ;
        String Model = " Model Name:\t" + android.os.Build.MODEL + "\n\n";
        String Brand = " Brand:\t" + Build.BRAND + "\n\n";
        String Manuf = " Manufacturer:\t" + Build.MANUFACTURER + "\n\n";
        String OsArch = " OS Architecture:\t" + System.getProperty("os.arch") + "\n\n";
        String OsName = " OS Name:\t" + System.getProperty("os.name") + "\n\n";
        String OsVersion = " OS Version:\t" + System.getProperty("os.version") + "\n\n";
        String OsVerRel = " Version Release:\t" + Build.VERSION.RELEASE + "\n\n";

        //MAC Address
        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifimanager.getConnectionInfo();
        String MACAddress = winfo.getMacAddress();

        //Location
        //Issues: Code is right, but emulator won't take it
        //Specifically the double lng and double lat lines.
        //LocationManager locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Location location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //double lng = location.getLongitude();
        //double lat = location.getLatitude();

        //Display every value in TextView
        TextView textView = new TextView(this);
        textView.setText(" Today's Date and Time: " + DateTime +"\n\n" + Device + Model + Brand + Manuf
                + OsArch +  OsName +  OsVersion + OsVerRel + "WiFi:\t" + wifi + "\n\n"
                + "MAC Address:\t" + MACAddress  ); //The part that displays the text on screen
        textView.setTextSize(15);  //Text size
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
