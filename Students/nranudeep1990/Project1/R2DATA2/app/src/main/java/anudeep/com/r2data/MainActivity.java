package anudeep.com.r2data;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    EditText ipAddress;
    EditText portNumber;
    Button   transferButton;
    Button   loadButton;
    TextView notificationView;
    String serverIp;
    int portNum;
    Socket conSocket;
    String Notification;
    BluetoothAdapter mAdapter;
    BluetoothSocket mSocket;
    OutputStream outStream;
    InputStream inputStream;
    String boxid;
    Dbcon db;
    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your server's MAC address
    private static String address = "C0:F8:DA:E3:6D:05";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipAddress = (EditText)findViewById(R.id.editText);
        portNumber = (EditText)findViewById(R.id.editText2);
        transferButton = (Button)findViewById(R.id.button);
        notificationView = (TextView)findViewById(R.id.textView);
        loadButton     = (Button)findViewById(R.id.button2);
        db = new Dbcon(getApplicationContext());
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverIp = ipAddress.getText().toString();
                ipAddress.setText("");
                portNum = Integer.parseInt(portNumber.getText().toString());
                portNumber.setText("");
                new SvrConnect(getApplicationContext()).execute();

            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PbConnect(getApplicationContext()).execute();

            }
        });

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

    class SvrConnect extends AsyncTask<Void, Void, Void> {
        Context context;

        public SvrConnect(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                conSocket = new Socket(serverIp,portNum);
                FileInputStream fis = null;
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                File myFile = new File(directory, "PB2.db");
                byte[] mybytearray = new byte[(int) myFile.length()];

                Notification = "File found.";
                Log.d("WriteTable", "file exists = " + myFile.exists());

                fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);

                Log.d("WriteTable", "Wrote value: " + myFile.length());


                bis.read(mybytearray, 0, mybytearray.length);
                OutputStream outToClient = conSocket.getOutputStream();
                outToClient.write(mybytearray, 0, mybytearray.length);
                outToClient.flush();
                outToClient.close();

                conSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            notificationView.setText("");
            notificationView.setText(Notification);
            Notification = "";

        }
    }

    class PbConnect extends AsyncTask<Void, Void, Void> {
        Context context;

        public PbConnect(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... args) {

            mAdapter = BluetoothAdapter.getDefaultAdapter();
//            if (mAdapter.isEnabled()) {
//                notificationView.setText("Bluetooth Enabled");
//            } else {
//                //Prompt user to turn on Bluetooth
//                Intent enableBtIntent = new Intent(mAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, 1);
//            }
            BluetoothDevice device = mAdapter.getRemoteDevice(address);

            // Two things are needed to make a connection:
            //   A MAC address, which we got above.
            //   A Service ID or UUID.  In this case we are using the
            //     UUID for SPP.
            try {
                mSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.getStackTraceString(e);
            }

            // Discovery is resource intensive.  Make sure it isn't going on
            // when you attempt to connect and pass your message.
            mAdapter.cancelDiscovery();

            // Establish the connection.  This will block until it connects.
            try {
                mSocket.connect();

            } catch (IOException e) {
                try {
                    mSocket.close();
                } catch (IOException e2) {
                    Log.getStackTraceString(e);
                }
            }

            try {
                outStream = mSocket.getOutputStream();
                inputStream = mSocket.getInputStream();
            } catch (IOException e) {
                Log.getStackTraceString(e);
            }


            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outStream));

                // Send the server my ID
                //long clientID = Math.abs(new HighQualityRandom().nextLong());
                bufferedWriter.write("R2Data2" + "\n");
                bufferedWriter.flush();


//                boxid = bufferedReader.readLine();
                boxid = "PB2";
                //notificationView.setText("PB Name is : "+boxid);

                String latestLine = db.getPbTime(boxid);
                bufferedWriter.write(latestLine + "\n");
                bufferedWriter.flush();


                latestLine = db.getSvrTime(boxid);
                bufferedWriter.write(latestLine + "\n");
                bufferedWriter.flush();

//                String lastRxTime = bufferedReader.readLine();
//                db.updatePbTime(boxid,lastRxTime);


//                 String recv = readInStream.readLine();
//                long fileLength = Long.parseLong(recv);
//                activity.updateText("File length I will receive is " + recv);

//                readInStream.close();
//                writeOutStream.close();

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
//                String filename = "testDB";


                try {
//                    File path = Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES);
//                    saveFile = new File(activity.getExternalFilesDir("data"), filename);
                    //File savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    File savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    saveFile = new File(savePath, boxid+".db");
                    //new File(activity.getExternalFilesDir("data"), filename);
                    Notification = "Received file into Downloads/"+boxid+".db";
                    is = inputStream;
                    os = new FileOutputStream(saveFile); // OS to write to file
                    streamsOpen = true;

                    byte[] b = new byte[2048];
                    int length;

                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                        downloadCounter += length;

                        //activity.updateText("Downloading: " + downloadCounter + "/" + filesize);
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

                bufferedWriter.close();
                bufferedReader.close();
                inputStream.close();
                outStream.close();
                mSocket.close();

            } catch (IOException e) {
                Log.getStackTraceString(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            notificationView.setText("");
            notificationView.setText(Notification);
            Notification = "";

        }
    }
}
