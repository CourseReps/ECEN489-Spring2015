package ironman.androidbluetooth;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class TNServerConnection implements Runnable {
    private int dbNumber = 0;
    public String DB_NAME;
    private MainActivity activity;
    private String serverIp;
    private int port = 9090;
    private Socket client;
    private BufferedReader reader;
    private String messsage = "Done";


    TNServerConnection (String ip, int newport) {

        this.serverIp = ip;
        this.port = newport;
    }

    public void run() {
        Log.d("Test", "connectToServer run");
        try {
            client = new Socket(serverIp, port);  //connect to server
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream outToClient = null;

            for (dbNumber = 0; dbNumber!=10; dbNumber++ ) {

                DB_NAME = "/storage/emulated/0/Documents/prombox"+dbNumber+".db";
                FileInputStream fis = null;
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File myFile = new File(DB_NAME);
                byte[] mybytearray = new byte[(int) myFile.length()];
                //activity.updateText("File found.");
                //Log.d("WriteTable", "file exists = " + myFile.exists());
                fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                //Log.d("WriteTable", "Wrote value: " + myFile.length());
                //activity.updateText("");
                bis.read(mybytearray, 0, mybytearray.length);
                outToClient = client.getOutputStream();
                outToClient.write(mybytearray, 0, mybytearray.length);
                outToClient.flush();
                outToClient.close();
                }

            client.close();

            // File sent, exit the main method
            return;
            //TODO: set lastid logic


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
