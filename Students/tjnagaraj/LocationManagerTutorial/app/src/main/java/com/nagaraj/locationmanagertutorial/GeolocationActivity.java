package com.nagaraj.locationmanagertutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class GeolocationActivity extends Activity {
    TextView latitudeText;
    TextView longitudeText;
    Context context =this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);
        latitudeText=(TextView)findViewById(R.id.latitude_text);
        longitudeText=(TextView)findViewById(R.id.longitude_text);
        LocationManager locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener= new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

    }
private void gpsAlert(){
    AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(context);
    alertDialogBuilder.setTitle("GPS Alert!");
    alertDialogBuilder
            .setMessage("GPS disabled! Press OK to enable.")
            .setCancelable(true)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, Redirect to GPS Settings
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                }
            })
            .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, just close the dialog box and do nothing
                    dialog.cancel();
                }
            });
    // create alert dialog
    AlertDialog alertDialog = alertDialogBuilder.create();

    // show it
    alertDialog.show();

}
    private class MyLocationListener implements LocationListener{
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            latitudeText.setText(Double.toString(latitude));
            longitudeText.setText(Double.toString(longitude));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
       gpsAlert();
       // startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);

    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geolocation, menu);
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
