package com.example.pranaykumar.androidsqlitenetworking;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Steps to using the sqlite DB:
 * 1. [DONE] Instantiate the DB Adapter
 * 2. [DONE] Open the DB
 * 3. [DONE] use get, insert, delete, .. to change data.
 * 4. [DONE]Close the DB
 */

public class MainActivity extends ActionBarActivity implements SensorEventListener { //  extends Activity


    private static final int UPDATE_Threshold = 5000;
    private static int sent_id = 1;
    private static int total_rows ;
    private long mLastUpdate;
    SensorManager sm;
    Sensor sensor;
    TextView displayReading;
    String message;

    String message2;
    DBAdapter myDb;

    Socket client;
    BufferedReader in;
    //PrintWriter out;
    //ObjectOutputStream objectOut;
    PrintWriter objectOut;
   // ObjectInputStream objectInput;
    String ip;
    String port;
    boolean STOP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDB();

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // Success! There is ACCELEROMETER. So get reference to Accelerometer
            sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            // failure! There's no ACCELEROMETER.
            finish();
        }
        displayReading = (TextView) findViewById(R.id.display_reading);
        //new chatClientConnect().execute();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // myDb.deleteAll();
        // message="Clearing Entire Table as the app is stopped ";
        // displayReading.setText(message);

        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }




    private void displayText(String message) {
        displayReading.setText(message);
    }



    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mLastUpdate = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);

    }


    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // Inflate the menu; this adds items to the action bar if it is present.

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // first we need to check the event corresponds to accelerometer reading
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long actualTime = System.currentTimeMillis();
            if (actualTime - mLastUpdate > UPDATE_Threshold) {
                mLastUpdate = actualTime;
                float x = event.values[0], y = event.values[1], z = event.values[2];
                //displayReading.setText(" X: "+event.values[0]+"\n Y: " +event.values[1]+"\n Z: "+event.values[2]);

                long newId = myDb.insertRow(x, y, z);
                total_rows=  (int) newId;


                Cursor cursor = myDb.getRow(newId);
                int id = cursor.getInt(DBAdapter.COL_ROWID);

                float X = cursor.getFloat(DBAdapter.COL_NAME);
                float Y = cursor.getFloat(DBAdapter.COL_STUDENTNUM);
                float Z = cursor.getFloat(DBAdapter.COL_FAVCOLOUR);

                // Append data to the message:
                message = (" ID=" + id + ",\n" + " ACC_X=" + X + ",\n ACC_Y=" + Y + ",\n ACC_Z=" + Z + "\n");

                if (id == 100) {
                    message = "Clearing Entire Table as entries crossed more than 100 ";
                    myDb.deleteAll();
                }

                //displayReading.setText(message);
                displayText(message);
            }
        }
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


    public void onClick_StopSending(View v) {

        STOP = true;

    }

    //private void serverInfo(String message2) {
    //    displayServer.setText(message2);
    //}
    public void onClick_SendSensorData(View v) {

        EditText editText = (EditText) findViewById(R.id.editText);
        ip = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editText2);
        port = editText.getText().toString();
        ConnectivityManager connMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connMan.getActiveNetworkInfo();
        //TextView notifier = (TextView) findViewById(R.id.textView2);
        if (network == null || !network.isConnected()) {
            // notifier.setText("       Check your Internet Connection");
            displayText("       Check your Internet Connection");
        } else if (ip.equalsIgnoreCase("") || port.equalsIgnoreCase("")) {
            // notifier.setText("       Enter a valid IP/PORT Number");
            displayText("       Enter a valid IP/PORT Number");
        }

        new chatClientConnect().execute();


    }


    class chatClientConnect extends AsyncTask<Void, Void, Void> {


        Integer Port = 0;
        @Override
        protected Void doInBackground(Void... args) {
            try {
               // TextView displayServer = (TextView) findViewById(R.id.display_server);
               // message2 = "sending recent data";
               // displayServer.setText(message2);
                if(sent_id==1) {
                    client = new Socket(ip, Port);
                    in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));

                    //objectOut = new ObjectOutputStream(client.getOutputStream());
                    objectOut = new PrintWriter(client.getOutputStream(), true);
                }
               // message = "sending recent data";
               // displayText(message);


                Cursor cursor = myDb.getRowafter((long) sent_id);

                do {
                    // Process the data:

                     int id = cursor.getInt(DBAdapter.COL_ROWID);
                    float x = cursor.getFloat(DBAdapter.COL_NAME);
                    float y = cursor.getFloat(DBAdapter.COL_STUDENTNUM);
                    float z = cursor.getFloat(DBAdapter.COL_FAVCOLOUR);
                       // objectOut.writeObject(Integer.toString(sent_id));
                    /*objectOut.writeObject(Integer.toString(id)+"\n");
                    objectOut.writeObject(Float.toString(x)+"\n");
                    objectOut.writeObject(Float.toString(y)+"\n");
                    objectOut.writeObject(Float.toString(z)+"\n");*/
                    if (STOP) {
                        objectOut.println("STOP");
                        break;
                    }
                    else  {
                        objectOut.println("RUN");
                    }
                    objectOut.println(Integer.toString(id));
                    objectOut.println(Float.toString(x));
                    objectOut.println(Float.toString(y));
                    objectOut.println(Float.toString(z));
                    // if (cursor.moveToNext()) {
                    //     objectOut.println("RUN");
                    // }


                    sent_id = sent_id + 1;
                    cursor.moveToNext();
                } while (sent_id <= total_rows);//while (cursor.moveToNext());

                objectOut.println("PAUSE");

                objectOut.flush();

               // message = "sent recent data";
               // displayText(message);



                if (STOP) {
                    objectOut.close();
                    in.close();
                    client.close();
                }


            } catch (Exception e) {
                //handle exception
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {


            super.onPostExecute(result);


        }

        @Override
        protected void onPreExecute() {


            //ip = intent.getStringExtra(MainActivity.IP_NUMBER);
             Port = Integer.parseInt(port);
        }

    }
}