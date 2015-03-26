package com.kcwd40.json_receiver;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.kcwd40.json_receiver";
    private EditText ipField;
    private EditText portField;
    static String serverIp;
    static int portNum;
    private InputStreamReader in;
    private Socket socket;
    static String text = null;
    static String devID = null;
    private boolean connected = false;

       /*     public void startFaceDetect(View view) {

            ipField = (EditText) findViewById(R.id.editText);
            portField = (EditText) findViewById(R.id.editText1);

                serverIp = ipField.getText().toString();
                ipField.setText("");
                portNum = Integer.parseInt(portField.getText().toString());
                portField.setText("");

               new connectToServer().execute();

               *//* connectToServer connect = new connectToServer(serverIp, portNum);
                Thread connectionThread = new Thread(connect);
                connectionThread.start();*//*

//                try {
//                    socket = new Socket(serverIp, portNum);  //connect to server
//                    connected = socket.isConnected();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                try {
//                    InputStreamReader readCommand = new InputStreamReader(socket.getInputStream());
//                    JsonReader reader = new JsonReader(readCommand);
//                    reader.beginObject();
//
//                    while(reader.hasNext()){
//                        String name = reader.nextName();
//                        if (name.equals("Command")) {
//                            text = reader.nextString();
//                        }
//                        else if(name.equals("DeviceID")){
//                            devID = reader.nextString();
//                        }
//                        else {
//                            reader.skipValue();
//                        }
//                    }
//
//
//                    if(text.equals("take picture")){
//                        Intent intent = new Intent(this, FaceDetect.class);
//                        intent.putExtra(EXTRA_MESSAGE, text);
//                        startActivity(intent);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                while (true) {
                    if ((connectToServer.text).equals("take picture")) {
                        Intent intent = new Intent(this, FaceDetect.class);
                        startActivity(intent);
                        System.out.println(text + "\n" + devID);
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }*/

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new connectToServer().execute();

        while (true) {

            try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            if ((connectToServer.text).equals("take picture")) {
                Intent intent = new Intent(this, FaceDetect.class);
                startActivity(intent);
                System.out.println(text + "\n" + devID);
                break;
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


}

