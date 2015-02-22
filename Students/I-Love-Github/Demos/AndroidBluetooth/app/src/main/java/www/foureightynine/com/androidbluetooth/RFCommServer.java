package www.foureightynine.com.androidbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class RFCommServer extends Thread {

    //based on java.util.UUID
    private static UUID MY_UUID = UUID.fromString("446118f0-8b1e-11e2-9e96-0800200c9a66");

    // The local server socket
    private BluetoothServerSocket mmServerSocket;

    // based on android.bluetooth.BluetoothAdapter
    private BluetoothAdapter mAdapter;
    private BluetoothDevice remoteDevice;

    private Activity activity;

    public RFCommServer(Activity activity) {
        this.activity = activity;
    }

    public void run() {
        BluetoothSocket socket = null;

        // Listen to the server socket if we're not connected
        while (true) {

            mAdapter = BluetoothAdapter.getDefaultAdapter();

            try {
                // Create a new listening server socket
                Log.d(this.getName(), "Initializing RFCOMM SERVER....");

                // MY_UUID is the UUID you want to use for communication

                mmServerSocket = mAdapter.listenUsingRfcommWithServiceRecord("Simple SPP", MY_UUID);
                //mmServerSocket = mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID); // you can also try using In Secure connection...

                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();

                Thread.sleep(100);
            } catch (Exception e) {

                Log.e(this.getName(), "Error creating socket: " + e.getMessage());
            }


            try {
                Log.d(this.getName(), "Closing Server Socket.....");
                mmServerSocket.close();

                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                // Get the BluetoothSocket input and output streams

                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();


                BufferedReader readInStream = new BufferedReader(new InputStreamReader(tmpIn));
                BufferedWriter writeOutStream = new BufferedWriter(new OutputStreamWriter(tmpOut));

                final String newText = readInStream.readLine();
                writeOutStream.write("This is a test string from the android device\n");
                writeOutStream.flush();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.relativeLayout);
                        TextView text = (TextView) layout.findViewById(R.id.textView);

                        text.setText(newText);
                    }
                });

                writeOutStream.close();
                readInStream.close();
                tmpIn.close();
                tmpOut.close();
                socket.close();

            } catch (Exception e) {

                Log.e(this.getName(), "Error creating socket: " + e.getMessage());
            }
        }
    }
}