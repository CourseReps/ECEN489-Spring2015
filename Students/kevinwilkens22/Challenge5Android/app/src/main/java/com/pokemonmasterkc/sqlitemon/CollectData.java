package com.pokemonmasterkc.sqlitemon;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import java.util.Date;


/**
 * Created by kwilk_000 on 2/15/2015.
 */
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
        //TODO: try to add more sensing data
        boolean exists = db.tableExists();
        Log.d("Database", "finished running tableExists");
        if (exists)
            Log.d("Database", "Table already exists from onCreate method");
        i = 0;
        int j = 0;
        while (j < 10) {
            //TODO: set loop condition
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
              j = MainActivity.stopSense;
            }
        }
        //TODO: set ipaddress field
//        ConnectToServer connect = new ConnectToServer("10.202.106.237", db);
//        Thread clientThread = new Thread(connect);
//        clientThread.start();
    }


}
