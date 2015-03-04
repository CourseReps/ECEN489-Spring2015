
package r2data4;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {
    String dbNum = null;
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
    Spinner BTmac;
    Spinner Filenum;
    Dbcon db;
    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your server's MAC address
//    private static String address = "C0:F8:DA:E3:6D:05";
    private String address;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipAddress = (EditText)findViewById(R.id.editText);
        portNumber = (EditText)findViewById(R.id.editText2);
        transferButton = (Button)findViewById(R.id.button);
        notificationView = (TextView)findViewById(R.id.textView);
        loadButton     = (Button)findViewById(R.id.button2);
        BTmac = (Spinner) findViewById(R.id.BTaddress);
        Filenum = (Spinner) findViewById(R.id.File);

//following code is for the bluetooth mac address spinner
        ArrayAdapter<CharSequence> BT = ArrayAdapter.createFromResource(this,
                R.array.Mac_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        BT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        BTmac.setAdapter(BT);

        BTmac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                address = BTmac.getSelectedItem().toString();
            }
        @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                address = "B4:B6:76:1E:6E:31";

            }

        });

        ArrayAdapter<CharSequence> Files = ArrayAdapter.createFromResource(this,R.array.File_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        Files.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Filenum.setAdapter(Files);

        Filenum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                name = Filenum.getSelectedItem().toString();

            }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            name = "PB1";

        }
        });

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
                int fileNum = 0;
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                //for loop to check how many files exist
                for(int i = 1; i<=4; i++){
                    if (new File(directory,"PB"+i+".db").exists()){
                        fileNum++;
                    }
                }
                //create the connection for the SVR
                conSocket = new Socket(serverIp,portNum);
                InputStream is = conSocket.getInputStream();
                OutputStream os = conSocket.getOutputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                OutputStream outToClient = null;
                //PrintWriter out = new PrintWriter(os, true);
                //out.println(fileNum);

                FileInputStream fis = null;
                File myFile = null;

                ///Loop for DB file senders

                    if (new File(directory,name+".db" ).exists()) {
                        myFile = new File(directory, name+".db");
                        byte[] mybytearray = new byte[(int) myFile.length()];
                        Notification = "File found.";
                        Log.d("WriteTable", "file exists = " + myFile.exists());
                        fis = new FileInputStream(myFile);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        Log.d("WriteTable", "Wrote value: " + myFile.length());
                        bis.read(mybytearray, 0, mybytearray.length);
                        //out.println(myFile.length());
///Hand Shake/////////////////////////////////////////////////////////////////////////
                        outToClient = conSocket.getOutputStream();
                        outToClient.write(mybytearray, 0, mybytearray.length);
                        outToClient.flush();
                        conSocket.shutdownOutput();
                        }
                String dataId = bufferedReader.readLine();
                Log.d("got:", dataId);
                db.updateSvrTime( name, dataId);
                outToClient.close();
                conSocket.close();
//              myFile.delete();

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

            BluetoothDevice device = mAdapter.getRemoteDevice(address);

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
                bufferedWriter.write("R2Data-4" + "\n");
                bufferedWriter.flush();


                boxid = bufferedReader.readLine();
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if(new File(directory,boxid+".db").exists()) {
                    new File(directory,boxid+".db").delete();

                }

                String latestLine = db.getPbTime(boxid);
                bufferedWriter.write(latestLine + "\n");
                bufferedWriter.flush();


                latestLine = db.getSvrTime(boxid);
                bufferedWriter.write(latestLine + "\n");
                bufferedWriter.flush();

                String lastRxTime = bufferedReader.readLine();
                db.updatePbTime(boxid,lastRxTime);


                String recv = bufferedReader.readLine();
                long fileLength = Long.parseLong(recv);



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

                try {

                    File savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    saveFile = new File(savePath, boxid+".db");
                    Notification = "Received file into Downloads/"+boxid+".db";
                    is = inputStream;
                    os = new FileOutputStream(saveFile); // OS to write to file
                    streamsOpen = true;

                    byte[] b = new byte[2048];
                    int length = 0;


                    while (downloadCounter<fileLength) {
                        length = is.read(b);
                        os.write(b, 0, length);
                        downloadCounter += length;

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