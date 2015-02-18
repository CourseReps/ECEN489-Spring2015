package trevor.com.challenge5android;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

/*
Class used to collect and store data from device sensors in local SQL database
*/

public class CollectData implements Runnable {

    //create instance variables
    private MainActivity parent;
    private DatabaseManager db;
    private final int collectTime = 1000; //used to set collection rate
    int count = 1; //initialize data point counter index 1

    //class constructor
    CollectData (MainActivity parent, DatabaseManager h) {
        this.parent = parent;
        this.db = h;
    }

    public void run() {
        //sets transmit button enable false which disables transmit button during data collection
        parent.transmitEnable(false);

        //get location service
        LocationManager locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);

        //method to check if table exists in database
        boolean exists = db.tableExists(); //TODO: fix tableExists method
        Log.d("Database", "finished running tableExists");
        if (exists)
            Log.d("Database", "Table already exists from onCreate method");

        //update status box on MainActivity
        parent.setStatusText("Now collecting data...");

        //loop breaks when stop is pushed
        while (parent.senseShowsStop) {
            try {
                //gets latitude and longitude coordinates
                Location coordinates = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                double lat = coordinates.getLatitude();
                double longitude = coordinates.getLongitude();

                //writes both coordinates to database
                db.addPoint(lat, longitude);

                //update status box and log
                parent.setStatusText("Now collecting data..." + "\nStored data point " + count);
                Log.d("CollectData", "Wrote value: " + count); //logs entry written
                Thread.sleep(collectTime); //record new data at collectTime intervals
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (SQLiteException e) {
                e.printStackTrace();
                Log.d("CollectData", "Error writing to database!");
                //parent.setStatusText("Error writing to database!");
            }
            finally {
                count++;
            }
        }
        //update status box
        parent.setStatusText("Finished collecting data!" + "\nEnter server IP address and press Transmit to send new data.");
        parent.transmitEnable(true);
    }
}
