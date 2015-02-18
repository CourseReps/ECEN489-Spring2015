package com.nagaraj.challenge5android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ConfigureActivity extends ActionBarActivity  {
EditText ipAddress_text;
    EditText portNumber_text;
    EditText clientName_text;
    Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        ipAddress_text=(EditText) findViewById(R.id.ipAddress);
        portNumber_text=(EditText)findViewById(R.id.portNumber);
        clientName_text=(EditText)findViewById(R.id.clientName);

    }

    public void onFinishButtonClick(View v){
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra("ipAddress", ipAddress_text.getText().toString());
        intent.putExtra("portNumber", Integer.parseInt(portNumber_text.getText().toString()));
        intent.putExtra("clientName",clientName_text.getText().toString());
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configure, menu);
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
