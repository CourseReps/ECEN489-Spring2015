package trevor.com.challenge5android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity {

    private Socket client;
    private PrintWriter printwriter;
    private EditText ipField;
    private TextView status;
    private String messsage;
    private String serverIp;
    static int LASTID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipField = (EditText) findViewById(R.id.editText);
        status = (TextView) findViewById(R.id.viewText);
        final DatabaseManager db = new DatabaseManager(this);
        final CollectData collector = new CollectData(this, db);


        Button senseButton =  (Button)findViewById(R.id.Sense);
        senseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread collectThread = new Thread(collector);
                collectThread.start();
                //TODO: Make status box
                //TODO: Change text colors
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}