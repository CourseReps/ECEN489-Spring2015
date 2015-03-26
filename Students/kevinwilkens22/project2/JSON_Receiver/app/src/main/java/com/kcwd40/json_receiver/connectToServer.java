package com.kcwd40.json_receiver;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.JsonReader;

import org.json.JSONObject;
import org.json.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by kwilk_000 on 3/25/2015.
 */

public class connectToServer extends AsyncTask<Void,Void,Void> {

    private String serverIp;
    private int port = 9090;
    //private Socket socket;
    static String text = null;
    static String devID = null;

    /*connectToServer (String ip, int port) {
        this.serverIp = ip;
        this.port = port;   //this.messsage = message;
    }*/

    public Void doInBackground(Void... params) {
        //Socket socket;
//        try {
//
//
////            connected = socket.isConnected();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
          Socket socket = new Socket("10.202.124.204", 9000);  //connect to server
            InputStreamReader readCommand = new InputStreamReader(socket.getInputStream());
//            BufferedReader readCommand = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //String line = readCommand.readLine();
            JsonReader reader = new JsonReader(readCommand);
            reader.beginObject();

            while(reader.hasNext()){
                String name = reader.nextName();
                if (name.equals("Command")) {
                    text = reader.nextString();
                    System.out.println(text);
                }
                else if(name.equals("DeviceID")){
                    devID = reader.nextString();
                    System.out.println(devID);
                }
                else {
                    reader.skipValue();
                }
                System.out.println(name);
            }
            reader.endObject();

           /* if(text.equals("take picture")){
                Intent intent = new Intent(MainActivity.this, FaceDetect.class);
                startActivity(intent);

                System.out.println(text + "\n" + devID);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
