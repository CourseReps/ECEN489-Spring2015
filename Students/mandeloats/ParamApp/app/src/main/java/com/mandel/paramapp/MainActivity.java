package com.mandel.paramapp;

import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String version = "SDK Version: "+ Build.VERSION.SDK_INT;
        String manufacturer = "Manufacturer: "+Build.MANUFACTURER;
        String model = "Model: "+Build.MODEL;
        String brand = "Brand: "+Build.BRAND;

        TextView manufacturerTextView = (TextView)findViewById(R.id.manufacturer);
        TextView modelTextView = (TextView)findViewById(R.id.model);
        TextView versionTextView = (TextView)findViewById(R.id.version);
        TextView brandTextView = (TextView)findViewById(R.id.brand);

        manufacturerTextView.setText(manufacturer);
        modelTextView.setText(model);
        versionTextView.setText(version);
        brandTextView.setText(brand);

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
