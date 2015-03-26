package com.nagaraj.facedetection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;

import java.io.InputStreamReader;
import java.net.Socket;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ConnectToServer().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
public void callFacedetection(){
    Intent intent = new Intent(this, DetectActivity.class);
    startActivity(intent);
    }

    public class ConnectToServer extends AsyncTask<Void,Void,Void> {
        private String serverIP="10.202.124.204";
        private  int port=9000;
         String text = null;
         String devID = null;

    /*connectToServer (String ip, int port) {
        this.serverIp = ip;
        this.port = port;   //this.messsage = message;
    }*/

        public Void doInBackground(Void... params) {
            //Socket socket;
//        try {
//
//
////            connected = socket.isConnected();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

            try {
                Socket socket = new Socket(serverIP, port);  //connect to server
                InputStreamReader readCommand = new InputStreamReader(socket.getInputStream());
                JsonReader reader = new JsonReader(readCommand);
                reader.beginObject();

                while(reader.hasNext()){
                    String name = reader.nextName();
                    if (name.equals("Command")) {
                        text = reader.nextString();
                        System.out.println(text);
                    }
                    else if(name.equals("DeviceID")){
                        devID = reader.nextString();
                        System.out.println(devID);
                    }
                    else {
                        reader.skipValue();
                    }
                    //   System.out.println(name);
                }
                reader.endObject();

                if(text.equals("take picture")){
                    callFacedetection();


                    System.out.println(text + "\n" + devID);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
