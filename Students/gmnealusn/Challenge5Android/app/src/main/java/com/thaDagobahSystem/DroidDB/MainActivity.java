package com.thaDagobahSystem.DroidDB;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;



public class MainActivity extends Activity {


    public static int stopSense = 0;

    private Socket client;
    private PrintWriter printwriter;
    private EditText ipField;
    private String messsage;
    private String serverIp;
    static int LASTID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = new TextView(this);
        textView.setTextSize(16);
        textView.setText("Tap the Begin Sensing button to write location to database.\n");


        ipField = (EditText) findViewById(R.id.editText);
        final DataBaseHandler db = new DataBaseHandler(this);
        final CollectData collector = new CollectData(this, db);


        Button senseButton =  (Button)findViewById(R.id.Sense);


        senseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread collectThread = new Thread(collector);
                collectThread.start();


            }
        });
        Button stopButton =  (Button)findViewById(R.id.Stop);
       stopButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    stopSense = 20;
                 }
             }
       );


        Button transmitButton = (Button)findViewById(R.id.Transmit);
        transmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverIp = ipField.getText().toString();
                ipField.setText("");
                ConnectToServer connect = new ConnectToServer(serverIp, db);
                Thread clientThread = new Thread(connect);
                clientThread.start();
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}