package trevor.com.assignment4android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Date;


public class PlatformInfo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_info);

        //Date and time
        Date currentDate = new Date();
        String date = "Date: " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentDate) + "\n";
        String time = "Time: " + DateFormat.getTimeInstance().format(currentDate) + "\n";

        //OS info
        String osname = "OS Name: " + System.getProperty("os.name") + "\n";
        String osversion = "OS Version: " + System.getProperty("os.version") + "\n";

        //Device info
        String device = "Device: " + Build.DEVICE + "\n";
        String manufacturer = "Manufacturer: " + Build.MANUFACTURER + "\n";
        String model = "Model: " + Build.MODEL + "\n";

        //Active connection info
        boolean wifiConnected;
        boolean mobileConnected;
        String connection = null;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if(wifiConnected) {
                WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                connection = "Currently connected to wifi\n" + "Info: " + wifiInfo.toString() + "\nSSID: " + wifiInfo.getSSID() + "\n";
            } else if (mobileConnected){
                connection = "Currently connected to cellular\n";
            }
        } else {
            connection = "No active internet connection\n";
        }

        //Orientation info
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orient =  display.getOrientation();
        String orientation = "Current screen orientation: " + orient;

        TextView text = (TextView)findViewById(R.id.info);

        text.setText(date + time + osname + osversion + device + model + manufacturer + connection + orientation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_platform_info, menu);
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
