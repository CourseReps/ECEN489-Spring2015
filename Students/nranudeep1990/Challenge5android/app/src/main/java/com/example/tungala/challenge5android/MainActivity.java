package com.example.tungala.challenge5android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    TextView transferredRecords;
    TextView insertedRecords;
    TextView senseNotification;
    TextView connectNotification;
    EditText ipNumber;
    EditText portNumber;
    Button startScan;
    Button stopScan;
    Button dbClear;
    Button connect;
    Button disConnect;
    InsertRecords insertRec;
    boolean scanflag = true;
    DbCon db;
    long lastRec = 0;
    Socket socket;
    String ip;
    int port;
    long transferCount = 0;
    String authUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transferredRecords = (TextView)findViewById(R.id.textView4);
        insertedRecords  = (TextView) findViewById(R.id.textView2);
        senseNotification = (TextView)findViewById(R.id.textView5);
        connectNotification = (TextView)findViewById(R.id.textView6);
        startScan = (Button)findViewById(R.id.button);
        stopScan = (Button)findViewById(R.id.button2);
        dbClear  = (Button)findViewById(R.id.button5);
        connect = (Button)findViewById(R.id.button3);
        disConnect = (Button)findViewById(R.id.button4);
        authUser = getAccountUsername();
        db = new DbCon(this);

        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanflag = true;
                senseNotification.setText("");
                senseNotification.setText("Sensing Started");
                new InsertRecords(getApplicationContext()).execute();

            }
        });

        stopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scanflag = false;
                senseNotification.setText("");
                senseNotification.setText("Sensing Stopped");

            }
        });
        dbClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                scanflag = false;
                db.deleteAll();
                lastRec = 0;
                senseNotification.setText("");
                senseNotification.setText("DataBase fully cleared");
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkConn();


            }
        });

        disConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    socket.close();
                    connectNotification.setText(connectNotification.getText().toString()+"\n"+"Connection closed Succesfully");
                }
                catch(Exception e){
                    connectNotification.setText("");
                    connectNotification.setText("Error in closing Connection");
                }

            }
        });

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        insertedRecords.setText("");
                        insertedRecords.setText(Long.toString(lastRec));
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000);


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

    class InsertRecords extends AsyncTask<Void, Void, Void> {

        Context context;

        public InsertRecords(Context context) {
            this.context = context;

        }

        @Override
        protected void onCancelled() {
            super.cancel(false);
            scanflag = false;
        }

        @Override
        protected Void doInBackground(Void... args) {

            WifiManager wifiManager;
            LocationManager lc;
            while(scanflag) {
                try {
                    wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    lc = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                    String location = lc.getAllProviders().toString();
                    String bssid = wifiManager.getConnectionInfo().getBSSID();
                    int rssi  = wifiManager.getConnectionInfo().getRssi();
                    lastRec = db.addRecord(location,bssid,rssi,"No");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }


            }

           return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            insertedRecords.setText("");
            insertedRecords.setText(Long.toString(lastRec));

        }

    }

    public void checkConn() {
        ipNumber = (EditText)findViewById(R.id.editText);
        portNumber = (EditText)findViewById(R.id.editText2);
        ip = ipNumber.getText().toString();
        port = Integer.parseInt(portNumber.getText().toString());
        WifiManager wifiManager;
        ConnectivityManager connMan=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network=connMan.getActiveNetworkInfo();

        if (network == null || !network.isConnected() ) {
            connectNotification.setText("Check your Internet Connection");
        }
        else if(ip.equalsIgnoreCase("")) {
            connectNotification.setText("Enter a valid IP/PORT Number");
        }
        else {
            new ClientConnect().execute();
        }

    }

    class ClientConnect extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... args) {
            try {

                socket = new Socket(ip,port);
                Cursor nonTransferredRecords = db.getUnsentRecords();
                nonTransferredRecords.moveToFirst();
                ObjectOutputStream out  = new ObjectOutputStream(socket.getOutputStream());
                transferCount = 0;
                ClientInfo ci = new ClientInfo();
                while(!nonTransferredRecords.isAfterLast()) {

                    long id = nonTransferredRecords.getInt(nonTransferredRecords.getColumnIndex("_id"));
                    String location = nonTransferredRecords.getString(nonTransferredRecords.getColumnIndex("location"));
                    String bssid  = nonTransferredRecords.getString(nonTransferredRecords.getColumnIndex("bssid"));
                    int rssi      = nonTransferredRecords.getInt(nonTransferredRecords.getColumnIndex("rssi"));
                    ci.setUsername(authUser);
                    ci.setLocation(location);
                    ci.setWifiBssid(bssid);
                    ci.setWifiRssid(rssi);
                    out.writeObject(ci);
                    out.flush();
                    db.updateTable(id);
                    transferCount++;
                    nonTransferredRecords.moveToNext();
                }




            }
            catch(Exception e) {
                connectNotification.setText("Connection Error");
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            transferredRecords.setText("");
            transferredRecords.setText(Long.toString(transferCount));
            connectNotification.setText("");
            connectNotification.setText("All Records Transferred");
            super.onPostExecute(result);


        }

    }

    public String getAccountUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {

            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }


}
