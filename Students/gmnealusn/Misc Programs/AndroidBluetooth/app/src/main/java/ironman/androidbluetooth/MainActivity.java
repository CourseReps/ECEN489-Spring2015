package ironman.androidbluetooth;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private EditText ipField;
    private EditText portField;
    private String serverIp;
    private int portNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**ipField = (EditText) findViewById(R.id.editText);

        Button transmitButton = (Button)findViewById(R.id.Transmit);
        transmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverIp = ipField.getText().toString();
                ipField.setText("");
                TNServerConnection connect = new TNServerConnection(serverIp, db);
                Thread clientThread = new Thread(connect);
                clientThread.start();
            }
        });**/

        ipField = (EditText) findViewById(R.id.editText);
        portField = (EditText) findViewById(R.id.editText1);

//TODO add status update for server connection
        Button transmitButton = (Button) findViewById(R.id.Transmit);
        transmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverIp = ipField.getText().toString();
                ipField.setText("");
                portNum = Integer.parseInt(portField.getText().toString());
                portField.setText("");
                TNServerConnection connect = new TNServerConnection(serverIp, portNum);
                Thread sendFileThread = new Thread(connect);
                sendFileThread.start();
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
