package ironman.androidbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Environment;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class RFCommServer extends Thread {
    private int dbNumber = 0;
    //Based on java.util.UUID
    private static UUID MY_UUID = UUID.fromString("446118f0-8b1e-11e2-9e96-0800200c9a66");

    // The local server socket
    private BluetoothServerSocket mmServerSocket;

    //Based on android.bluetooth.BluetoothAdapter
    private BluetoothAdapter mAdapter;
    private BluetoothDevice remoteDevice;

    private MainActivity activity;

    public RFCommServer(MainActivity activity) {
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
                Log.d(this.getName(), "Communicating over Bluetooth Server Socket.....");
                mmServerSocket.close();

                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                // Get the BluetoothSocket input and output streams

                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();

                BufferedReader readInStream = new BufferedReader(new InputStreamReader(tmpIn));
                BufferedWriter writeOutStream = new BufferedWriter(new OutputStreamWriter(tmpOut));
                Log.d(this.getName(), "Socket connection successful");

//                Thread.sleep(500);

                // Send the server my ID
                long clientID = Math.abs(new HighQualityRandom().nextLong());
                writeOutStream.write(String.valueOf(clientID) + "\n");
                writeOutStream.flush();
                Log.d(this.getName(), "Wrote clientID to PC");

                String recv = readInStream.readLine();
                // CHECK client ID and see what the latest DB line we have
                activity.updateText("Promiscuous Box ID is " + recv);

                long latestLine = 0;
                writeOutStream.write(String.valueOf(latestLine) + "\n");
                writeOutStream.flush();
                Log.d(this.getName(), "Wrote latestline to PC");

                recv = readInStream.readLine();
                long fileLength = Long.parseLong(recv);
                activity.updateText("File length I will receive is " + recv);


                /////////////////////////////////////////////////////////////////////////////////
                // BEGIN DOWNLOAD CODE //////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////////////////////
                InputStream is = null;
                OutputStream os = null;
                File saveFile = null;
                int downloadCounter = 0;
                int downloadTotal = 0;
                boolean streamsOpen = false;

                int filesize = 0;
                String filename = "testDB";

                try {
                    if(dbNumber == 0) {
                        saveFile = new File("/storage/emulated/0/Documents/prombox" + dbNumber + ".db");
                        dbNumber++;
                    }
                    else if(dbNumber > 0 && dbNumber < 10){
                        saveFile = new File("/storage/emulated/0/Documents/prombox" + dbNumber + ".db");
                        dbNumber++;
                    }
                    else if(dbNumber == 10){
                        dbNumber = 0;
                        saveFile = new File("/storage/emulated/0/Documents/prombox" + dbNumber + ".db");
                        dbNumber++;
                    }

                    is = tmpIn;
                    os = new FileOutputStream(saveFile); // OS to write to file
                    streamsOpen = true;

                    byte[] b = new byte[2048];
                    int length;

                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                        downloadCounter += length;

                        activity.updateText("Downloading: " + downloadCounter + "/" + filesize);
                    }

                } catch (FileNotFoundException fnfe) {
                    Log.e("UH OH", Log.getStackTraceString(fnfe));
                } catch (IOException ioe) {
                    Log.e("UH OH", Log.getStackTraceString(ioe));
                } catch (Exception e) {
                    if (streamsOpen) {
                        Log.e("UH OH", Log.getStackTraceString(e));
                        saveFile.delete();
                    }
                    Log.w("D/L THREAD WARNING", "The current download has been stopped by another process");
                } finally {
                    if (streamsOpen) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            Log.e("UH OH", Log.getStackTraceString(e));
                        }
                        try {
                            os.close();
                        } catch (Exception e) {
                            Log.e("UH OH", Log.getStackTraceString(e));
                        }
                    }
                }

                /////////////////////////////////////////////////////////////////////////////////
                // END DOWNLOAD CODE ////////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////////////////////

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