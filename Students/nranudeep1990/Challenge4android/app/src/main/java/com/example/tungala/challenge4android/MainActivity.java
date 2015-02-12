package com.example.tungala.challenge4android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    public final static String IP_NUMBER = "com.example.tungala.challenge4android.ip";
    public final static String PORT = "com.example.tungala.challenge4android.port";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    //method to initiate a connection to server
    public void connectToServer(View view){

        EditText editText = (EditText) findViewById(R.id.editText);
        String ip  = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editText2);
        String port = editText.getText().toString();
        ConnectivityManager connMan=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network=connMan.getActiveNetworkInfo();
        TextView notifier = (TextView) findViewById(R.id.textView2);
        if (network == null || !network.isConnected() ) {
            notifier.setText("       Check your Internet Connection");
        }
        else if(ip.equalsIgnoreCase("")|| port.equalsIgnoreCase("")) {
            notifier.setText("       Enter a valid IP/PORT Number");
        }
        else {
            Intent i = new Intent(this,Chat.class);
            i.putExtra(IP_NUMBER,ip);
            i.putExtra(PORT,port);
            startActivity(i);
        }

    }
}
