package com.kcwmasterpiece.databasesender;

/**
 * Created by kwilk_000 on 2/22/2015.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectToServer implements Runnable {

    private String serverIp;
    private int port = 9090;
    private Socket client;
    private PrintWriter printwriter;
    private BufferedReader reader;
    private String messsage = "Done";
    private DataBaseHandler db;
    private int lastId;
    private int entries;

    ConnectToServer (String ip, DataBaseHandler h) {
        this.serverIp = ip;
        this.db = h;
        //this.messsage = message;
    }

    public void run() {
        Log.d("Test", "connectToServer run");
        try {
            client = new Socket(serverIp, port);  //connect to server
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream outToClient = null;

            //wait for server ready message
/*            String received = null;
            received = reader.readLine();*/

                    FileInputStream fis = null;


                    File myFile = new File(DataBaseHandler.DB_PATH);
                    byte[] mybytearray = new byte[(int) myFile.length()];
                    Log.d("WriteTable", "file exists = " + myFile.exists());
                    fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    Log.d("WriteTable", "Wrote value: " + myFile.length());
                    bis.read(mybytearray, 0, mybytearray.length);

                    outToClient = client.getOutputStream();

                    outToClient.write(mybytearray, 0, mybytearray.length);
                    outToClient.flush();
                    outToClient.close();
                    client.close();

                    // File sent, exit the main method
                    return;
            //TODO: set lastid logic
           /* String sqlData = null;
            lastId = MainActivity.LASTID;

            //Log.d("ToServer", "Sent ID: " + lastId);
            entries = lastId + db.entries;
            //while (!(sqlData = db.getData(lastId)).equals(null)) {
            while (lastId < entries) {
                sqlData = db.getData(lastId);
                if (sqlData.equals(null))
                    break;
                printwriter.println(sqlData);
                Log.d("ToServer", "Sent ID: " + lastId);
                lastId++;
            }

            MainActivity.LASTID = lastId;

            //db.getdata

            printwriter.write(messsage);  //write the message to output stream
            printwriter.flush();
            printwriter.close();
            client.close();   //closing the connection*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
