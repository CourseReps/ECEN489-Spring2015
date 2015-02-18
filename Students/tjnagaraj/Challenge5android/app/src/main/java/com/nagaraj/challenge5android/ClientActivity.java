package com.nagaraj.challenge5android;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ClientActivity extends ActionBarActivity implements SensorEventListener{

    SensorManager sensorManager;
    Sensor sensor;
    protected Context context;
    TextView displayLastTransferTime;
    TextView displayX;
    TextView displayY;
    TextView displayZ;
    TextView displayNumberOfNewRecords;
    TextView displayAlert;
    int update_interval = 3000;
    long lastUpdateTime ;
    DatabaseManager myLocalDB;
    long lastTransferredRowId;
    long lastId =0;
    String ipAddress="192.168.0.34";
    String portNumber="9000";
    Socket client;
    ObjectOutputStream objectOut;
    ClientInfoPacket clientInfoPacket;
    String lastTransferredTime=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        lastTransferredRowId=0;
        displayNumberOfNewRecords= (TextView) findViewById(R.id.newRecords_text);
        displayX = (TextView) findViewById(R.id.X_textview);
        displayY = (TextView) findViewById(R.id.Y_textview);
        displayZ = (TextView) findViewById(R.id.Z_textview);
        displayAlert = (TextView) findViewById(R.id.networkAlert_textview);
        displayLastTransferTime = (TextView) findViewById(R.id.time_textview);
        openDB();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {

            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        } else {
            //No Sensor found.

            displayAlert.setText("No ORIENTATION SENSOR found!");
            finish();
        }

        //Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        //displayDate.setText("Current date: "+formattedDate);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        if(currentTime-lastUpdateTime > update_interval){
            lastUpdateTime=currentTime;
            Calendar c=Calendar.getInstance();
            SimpleDateFormat df =new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            String formattedDate =df.format(c.getTime());

            if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
                displayX.setText(Float.toString(event.values[0]));
                displayY.setText(Float.toString(event.values[1]));
                displayZ.setText(Float.toString(event.values[2]));
                lastId = myLocalDB.insertRow(event.values[0],event.values[1],event.values[2],formattedDate,"No");
                System.out.println("lastId:  " + lastId);
                int newRecords = (int) (lastId-lastTransferredRowId);
                //System.out.println("newRecords   :"+newRecords);
                displayNumberOfNewRecords.setText(Integer.toString(newRecords));
                displayLastTransferTime.setText(lastTransferredTime);

            }

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myLocalDB = new DatabaseManager(this);
        myLocalDB.open();
    }

    private void closeDB() {
        myLocalDB.close();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onClick_TransferData(View v){
        ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isConnected()){
            displayAlert.setText("Check your Network Connection!");
        }
        new Transfer(lastId).execute();

    }


    @Override

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor,SensorManager.SENSOR_STATUS_ACCURACY_LOW );
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }



    class Transfer extends AsyncTask<Void, Void, Void> {
        long lastId;
        ClientOutMessage clientOutMessage;

        public Transfer(long lastId) {
            this.lastId= lastId;
        }
        @Override
        protected Void doInBackground(Void... args) {
            try {

                client = new Socket(ipAddress, Integer.parseInt(portNumber));

                objectOut = new ObjectOutputStream(client.getOutputStream());

                Cursor cursor = myLocalDB.getRowsGreaterThan((long)lastTransferredRowId );
                int count=1;
                do {

                    if(count==1) {
                        clientInfoPacket = new ClientInfoPacket();
                        clientInfoPacket.setClientName("Nagaraj_test2");
                        clientInfoPacket.setMode(true);
                        objectOut.writeObject(clientInfoPacket);
                        objectOut.flush();
                    }

                    count++;
                    ClientOutMessage clientOutMessageTemp = new ClientOutMessage();
                    clientOutMessageTemp.id = cursor.getInt(DatabaseManager.COL_ROWID);
                    clientOutMessageTemp.X = cursor.getDouble(DatabaseManager.COL_X);
                    clientOutMessageTemp.Y = cursor.getDouble(DatabaseManager.COL_Y);
                    clientOutMessageTemp.Z = cursor.getDouble(DatabaseManager.COL_Z);
                    clientOutMessageTemp.date = cursor.getString(DatabaseManager.COL_DATE_TIME);

                    String flag = cursor.getString(DatabaseManager.COL_FLAG);
                    clientOutMessage=clientOutMessageTemp;
                    objectOut.writeObject(clientOutMessage);
                    myLocalDB.updateRow(clientOutMessageTemp.id,clientOutMessageTemp.X,clientOutMessageTemp.Y,clientOutMessageTemp.Z,clientOutMessageTemp.date,"YES");

                    lastTransferredRowId++;
                   if(lastTransferredRowId<=this.lastId)
                    cursor.moveToNext();
                } while (lastTransferredRowId <= this.lastId );
                lastTransferredTime=clientOutMessage.date;

                objectOut.flush();
                objectOut.close();
                client.close();

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
        }

    }


}
