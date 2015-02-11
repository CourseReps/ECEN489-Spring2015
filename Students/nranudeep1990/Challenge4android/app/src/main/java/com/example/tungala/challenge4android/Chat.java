package com.example.tungala.challenge4android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class Chat extends ActionBarActivity {

    Socket client;
    TextView messageArea;
    Button sendButton;
    Button clearButton;
    Button disconButton;
    EditText msgText;
    private String message;
    BufferedReader in;
    PrintWriter out;
    ObjectOutputStream objectOut;
    ObjectInputStream objectInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageArea = (TextView)findViewById(R.id.textView3);
        msgText     = (EditText)findViewById(R.id.editText3);
        sendButton = (Button)findViewById(R.id.button4);
        clearButton = (Button)findViewById(R.id.button3);
        disconButton = (Button)findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = msgText.getText().toString();
                msgText.setText("");
                new chatClientMessage(getApplicationContext()).execute();
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageArea.setText("");
            }
        });
        disconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    out.println("Closing Connection");
                    out.flush();
                    in.close();
                    client.close();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                Intent i = new Intent(Chat.this,MainActivity.class);
                Chat.this.startActivity(i);

            }
        });
        new chatClientConnect().execute();

//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        new chatClientLoop(getApplicationContext()).execute();
//
//                    }
//                });
//            }
//        };
//        timer.schedule(task, 0, 1000);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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

    class chatClientConnect extends AsyncTask<Void, Void, Void> {

        String respmsg = " ";
        String ip = " ";
        Integer port = 0;
        @Override
        protected Void doInBackground(Void... args) {
            try {

                client = new Socket(ip,port);

                in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);


                WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                LocationManager lc = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                ClientInfo ci = new ClientInfo();
//                Criteria criteria = new Criteria();
//                String bestProvider = lc.getBestProvider(criteria, false);
//                Location location = lc.getLastKnownLocation(bestProvider);
                ci.setLocation(lc.getAllProviders().toString());
                ci.setWifiBssid(wifiManager.getConnectionInfo().getBSSID());
                ci.setWifiRssid(wifiManager.getConnectionInfo().getRssi());



                // Consume the initial welcoming messages from the server
                for (int i = 0; i < 3; i++) {
                   respmsg = respmsg+" "+in.readLine() + "\n";
                }

                objectOut = new ObjectOutputStream(client.getOutputStream());
                objectOut.writeObject(ci);
                objectOut.flush();




            }
            catch(Exception e) {
                //handle exception
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

           messageArea.setText(respmsg);
            super.onPostExecute(result);


        }

        @Override
        protected void onPreExecute() {

            Intent intent = getIntent();
            ip = intent.getStringExtra(MainActivity.IP_NUMBER);
            port      = Integer.parseInt(intent.getStringExtra(MainActivity.PORT));
        }

    }

    class chatClientMessage extends AsyncTask<Void, Void, Void> {

        Context context;
        public chatClientMessage(Context context){
            this.context = context;
        }
        @Override
        protected Void doInBackground(Void... args) {
            try {
                out.println(message);
                WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                LocationManager lc = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                ClientInfo ci = new ClientInfo();
                ci.setLocation(lc.getAllProviders().toString());
                ci.setWifiBssid(wifiManager.getConnectionInfo().getBSSID());
                ci.setWifiRssid(wifiManager.getConnectionInfo().getRssi());
                objectOut.writeObject(ci);
                objectOut.flush();

            }
            catch(Exception e) {
                //handle exception
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(message.equalsIgnoreCase(":q")) {
                Intent i = new Intent(context,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            else {
                messageArea.setText(messageArea.getText().toString()+"\n"+message);
            }


        }

    }

    class chatClientLoop extends AsyncTask<Void, Void, Void> {

        Context context;
        public chatClientLoop(Context context){
            this.context = context;
        }
        @Override
        protected Void doInBackground(Void... args) {
            try {
                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                ClientInfo ci = new ClientInfo();
                ci.setLocation("USA");
                ci.setWifiBssid(wifiManager.getConnectionInfo().getBSSID());
                ci.setWifiRssid(wifiManager.getConnectionInfo().getRssi());
                objectOut = new ObjectOutputStream(client.getOutputStream());
                objectOut.writeObject(ci);
                objectOut.close();

            }
            catch(Exception e) {
                //handle exception
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}
