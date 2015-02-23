package com.kcwmasterpiece.databasesender;

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
    private String dataCollectionStatus;
    private String serverIp;
    static int LASTID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//TODO Add status update for collection

        ipField = (EditText) findViewById(R.id.editText);
        final DataBaseHandler db = new DataBaseHandler(this);
        final CollectData collector = new CollectData(this, db);

        TextView dcStatus = (TextView) findViewById(R.id.collection_status);
        Button senseButton = (Button) findViewById(R.id.Sense);


        senseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                stopSense = 0;
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

        dcStatus.setText(CollectData.dataStatus);

        Button stopButton = (Button) findViewById(R.id.Stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              stopSense = 20;
                                          }
                                      }
        );

        dcStatus.setText(CollectData.dataStatus);
//TODO add status update for server connection
        Button transmitButton = (Button) findViewById(R.id.Transmit);
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
