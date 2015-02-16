package trevor.com.challenge5android;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class CollectData implements Runnable {

    private MainActivity parent;
    private DatabaseManager db;

    CollectData (MainActivity parent, DatabaseManager h) {
        this.parent = parent;
        this.db = h;
    }

    public void run() {
        LocationManager locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
        boolean exists = db.tableExists();
        Log.d("Database", "finished running tableExists");
        if (exists)
            Log.d("Database", "Table already exists from onCreate method");
        int i = 0;
        while (i < 10) {
            //TODO: set loop condition
            try {
                //long date = new Date().getTime();
                Location coordinates = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                double lat = coordinates.getLatitude();
                double longitude = coordinates.getLongitude();
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
