package com.pokemonmasterkc.sqlitemon;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.Date;
public class CollectData implements Runnable {

    private MainActivity parent;
    private DataBaseHandler db;
    private int i = 0;

    CollectData (MainActivity parent, DataBaseHandler h) {
        this.parent = parent;
        this.db = h;
    }

    public void run() {
        LocationManager locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
        // Location latlong = new Location(locationManager.NETWORK_PROVIDER);
        // Use Network location data:
        boolean exists = db.tableExists();
        Log.d("Database", "finished running tableExists");
        if (exists)
            Log.d("Database", "Table already exists from onCreate method");
        i = 0;
        while (i < 10) {
            try {
                //long date = new Date().getTime();
                Location loc1 = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                double lat = loc1.getLatitude();
                double longitude = loc1.getLongitude();
                db.add(lat, longitude);

                Log.d("WriteTable", "Wrote value: " + i);
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (SQLiteException e) {
                e.printStackTrace();
            }
            finally {
                i++;
            }
        }
    }


}
