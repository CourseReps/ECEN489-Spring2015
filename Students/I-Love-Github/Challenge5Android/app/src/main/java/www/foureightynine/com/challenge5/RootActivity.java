package www.foureightynine.com.challenge5;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;


public class RootActivity extends Activity {

    private ClientRunnable clientRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        ( (TextView) findViewById(R.id.first)).setText("This is a test");
    }

    @Override
    protected void onStart() {
        super.onStart();
        clientRunnable = new ClientRunnable(this);
        Thread clientThread = new Thread(clientRunnable);
        clientThread.start();
    }

    public void newMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.first)).append(message + "\n");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        clientRunnable.getDB().killDB();
        clientRunnable.getCommHandler().disconnect();
    }
}
