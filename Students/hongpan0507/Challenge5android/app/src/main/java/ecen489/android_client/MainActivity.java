package ecen489.android_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    private String ip_address = "127.0.0.1";
    private int port_numb = 2015;

    private TextView TV_port_numb;
    private TextView TV_server_ip;
    private TextView TV_wifi_status;
    private EditText ET_msg;
    private Button butt_ip;
    private Button butt_port_numb;
    private Button butt_start_client;

    private String temp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up server
        TV_server_ip = (TextView) findViewById(R.id.TV_server_ip);
        TV_port_numb = (TextView) findViewById(R.id.TV_port_numb);
        ET_msg = (EditText) findViewById(R.id.ET_msg);

        butt_ip = (Button) findViewById(R.id.butt_ip);
        butt_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ip_address = ET_msg.getText().toString();
                TV_server_ip.setText("Server IP Address: " + ip_address);
            }
        });

        butt_port_numb = (Button) findViewById(R.id.butt_port_numb);
        butt_port_numb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                temp = ET_msg.getText().toString();
                TV_port_numb.setText("Port Number: " + temp);
                port_numb = Integer.parseInt(temp);
            }
        });

        TV_wifi_status = (TextView) findViewById(R.id.TV_wifi_status);
        butt_start_client = (Button) findViewById(R.id.butt_start_client);
        ConnectivityManager cm = (ConnectivityManager)super.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            butt_start_client.setEnabled(true);
            TV_wifi_status.setText("Wifi connected");
        } else {
            butt_start_client.setEnabled(false);
            TV_wifi_status.setText("Wifi not available");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //call when "start_client" button is clicked
    public void start_client(View view){	//start up the server
        Intent i = new Intent(this,client_activity.class);
        Bundle extras = new Bundle();
        extras.putInt("port_numb", port_numb);
        extras.putString("ip_address", ip_address);
        i.putExtras(extras);
        startActivity(i);	//intent is the screen that pops up
    }
}
