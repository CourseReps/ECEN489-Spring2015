package trevor.com.challenge5android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity {

    //create instance variables
    private EditText ipField;
    private TextView status;
    private String serverIp;
    private Button senseButton;
    private Button transmitButton;


    //static class variables
    static int LASTID = 0;  //static variable used to keep track of last transmitted database entry
    static boolean senseShowsStop = false;  //static variable used start and stop data collection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create class objects
        final DatabaseManager db = new DatabaseManager(this, this);
        final CollectData collector = new CollectData(this, db);
        final ConnectToServer connection = new ConnectToServer(this, db);

        //identify status text and edit text boxes
        ipField = (EditText) findViewById(R.id.editText);
        status = (TextView) findViewById(R.id.viewText);
        status.setText("Press Sense to begin collecting data...");

        //identify sense and transmit buttons
        senseButton =  (Button)findViewById(R.id.Sense);
        transmitButton = (Button)findViewById(R.id.Transmit);

        senseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if statement checks if data collection is in progress; if not, start the collection
                if (!senseShowsStop) {
                    setSenseText("Stop");
                    senseShowsStop = true;
                    Thread collectThread = new Thread(collector);
                    collectThread.start();
                }

                //stops data collection by setting boolean to false. Referenced in CollectData class
                else if (senseShowsStop) {
                    setSenseText("Sense");
                    senseShowsStop = false;
                }
            }
        });

        transmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverIp = ipField.getText().toString();
                connection.setServerIp(serverIp);
                if (serverIp.equals(""))
                    setStatusText("Enter valid IP address!");
                //only starts server connection if database has recorded new entries
                else if (!(db.entries == 0)) {
                    Thread clientThread = new Thread(connection);
                    clientThread.start();
                }
                //otherwise reports to user no new data
                else if (db.entries == 0)
                    setStatusText("No new data to transmit!\nPress sense to collect new data!");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //method used to enable sense button
    public void senseEnable (final boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                senseButton.setEnabled(b);
            }
        });
    }

    //method used to change text displayed on sense button
    public void setSenseText (final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                senseButton.setText(text);
            }
        });
    }

    //method used to set status box text
    public void setStatusText (final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText(message);
            }
        });
    }

    //method used to enable transmit button
    public void transmitEnable (final boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                transmitButton.setEnabled(b);
            }
        });
    }
}