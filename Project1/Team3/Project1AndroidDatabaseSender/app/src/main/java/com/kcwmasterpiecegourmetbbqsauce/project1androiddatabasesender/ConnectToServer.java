package com.kcwmasterpiecegourmetbbqsauce.project1androiddatabasesender;

/**
 * Created by kwilk_000 on 2/25/2015.
 */
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
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

    private MainActivity activity;
    private static String DB_NAME = "MACAddressInfo.db";
    private String serverIp;
    private int port = 9090;
    private Socket client;
    private BufferedReader reader;
    private String messsage = "Done";


    ConnectToServer (String ip, int port) {

        this.serverIp = ip;
        this.port = port;
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
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File myFile = new File(directory, DB_NAME);
            byte[] mybytearray = new byte[(int) myFile.length()];

            activity.updateText("File found.");
            Log.d("WriteTable", "file exists = " + myFile.exists());

            fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            Log.d("WriteTable", "Wrote value: " + myFile.length());
            activity.updateText("");

            bis.read(mybytearray, 0, mybytearray.length);
            outToClient = client.getOutputStream();
            outToClient.write(mybytearray, 0, mybytearray.length);
            outToClient.flush();
            outToClient.close();

            /*String fileReceivedConfirmation = reader.readLine();
            if(fileReceivedConfirmation.equals("Server Received File Successfully")){

            try{
                new File(directory, DB_NAME).delete();
                }
                catch (Exception e){
                Log.e("DeletionError", "File Couldn't be Deleted");
                e.printStackTrace();
                }
                activity.updateText("File Transfer Completed");
                Log.d("Update", "File Transfer Completed.");
            }
            else{
            activity.updateText("File Transfer Unconfirmed");
            Log.d("Check", "File Transfer Unconfirmed");
            }*/

            client.close();

            // File sent, exit the main method
            return;
            //TODO: set lastid logic


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
