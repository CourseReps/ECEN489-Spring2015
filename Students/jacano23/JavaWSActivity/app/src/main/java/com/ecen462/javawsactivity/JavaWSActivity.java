package com.ecen462.javawsactivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class JavaWSActivity extends Activity {

    CheckInClient checkIn;

    Button b;
    TextView tv;
    //EditText et;
    ProgressBar pg;
    String editText;
    String displayText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Name Text control
        //et = (EditText) findViewById(R.id.editText1);
        //Display Text control
        tv = (TextView) findViewById(R.id.tv_result);
        //Button to trigger web service invocation
        b = (Button) findViewById(R.id.button1);
        //Display progress bar until web service invocation completes
        pg = (ProgressBar) findViewById(R.id.progressBar1);
        //Button Click Listener
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                /*
                JSONObject object = new JSONObject();
                try {
                    object.put("timestamp", "3453895");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //editText = "{\"timestamp\":\"123456\"}";
                editText = object.toString();
                */
                AsyncCallWS task = new AsyncCallWS();
                task.execute();

            }
        });
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //checkIn.JsonHandler();
//            displayText = WebServiceHttps.invokeHelloWorldWS(editText,"getServerResponse");
            displayText = CheckInClient.JsonHandler();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            tv.setText(displayText);
            pg.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            pg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dot_net_w, menu);
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
