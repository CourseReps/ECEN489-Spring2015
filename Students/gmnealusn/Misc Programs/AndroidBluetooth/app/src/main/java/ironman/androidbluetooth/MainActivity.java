package ironman.androidbluetooth;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends ActionBarActivity {

    private EditText ipField;
    private EditText portField;
    private String serverIp;
    private int portNum;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.Transmit); // reference to the send button
        ipField = (EditText) findViewById(R.id.editText);
        portField = (EditText) findViewById(R.id.editText1);

        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
             serverIp = ipField.getText().toString();
             ipField.setText("");
             portNum = Integer.parseInt(portField.getText().toString());
             portField.setText("");
             TNServerConnection connect = new TNServerConnection(serverIp, portNum);
             Thread sendFileThread = new Thread(connect);
             sendFileThread.start();
             //messsage = textField.getText().toString(); // get the text message on the text field
             //textField.setText(""); // Reset the text field to blank
               // SendMessage sendMessageTask = new SendMessage();
               // sendMessageTask.execute();
                //executor.execute(new sendFile());
            }
        });
    }





    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new RFCommServer(this)).start();
    }
  /**  @Override
        protected void onStart() {
        super.onStart();
        new Thread(new TNServerConnection(this)).start();
    }
**/
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


    public void updateText(final String newText) {

    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);
            TextView text = (TextView) layout.findViewById(R.id.textView);

            text.setText(newText);
        }
    });
    }
}
