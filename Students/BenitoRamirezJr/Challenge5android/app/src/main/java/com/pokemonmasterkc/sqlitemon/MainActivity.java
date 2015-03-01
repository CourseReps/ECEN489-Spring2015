package com.pokemonmasterkc.sqlitemon;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity {

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

        ipField = (EditText) findViewById(R.id.editText);
        final DataBaseHandler db = new DataBaseHandler(this);
        final CollectData collector = new CollectData(this, db);


        Button senseButton =  (Button)findViewById(R.id.Sense);
        senseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread collectThread = new Thread(collector);
                collectThread.start();
                //Log.d("System", "Finished Sending Data");

                //Log.d("Insert: ", "Inserting ..");
                //db.add(new Values("value1"));
//                db.add(new Values("value2"));
//                db.add(new Values("value3"));
//                db.add(new Values("value4"));
                //Log.d("Insert", "DataBase Successfully Updated");

            }
        });

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


//        messsage = db.readDB();
//        ConnectToServer connect = new ConnectToServer(serverIp, messsage);
//        Thread clientThread = new Thread(connect);
//        clientThread.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}