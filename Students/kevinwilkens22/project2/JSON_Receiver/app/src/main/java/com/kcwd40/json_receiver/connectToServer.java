package com.kcwd40.json_receiver;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by kwilk_000 on 3/25/2015.
 */
public class connectToServer extends AsyncTask <Void,Void,Void>{
    public final static String EXTRA_MESSAGE = "com.kcwd40.json_receiver";

    private Socket socket;
    static String text = null;
    static String devID = null;

    protected Void doInBackground(Void... params) {

        try {
            socket = new Socket(MainActivity.serverIp, MainActivity.portNum);  //connect to server
//            connected = socket.isConnected();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStreamReader readCommand = new InputStreamReader(socket.getInputStream());
            JsonReader reader = new JsonReader(readCommand);
            reader.beginObject();

            while(reader.hasNext()){
                String name = reader.nextName();
                if (name.equals("Command")) {
                    text = reader.nextString();
                }
                else if(name.equals("DeviceID")){
                    devID = reader.nextString();
                }
                else {
                    reader.skipValue();
                }
            }


            if(text.equals("take picture")){
                /*Intent intent = new Intent(MainActivity.this, FaceDetect.class);
                intent.putExtra(EXTRA_MESSAGE, text);
                startActivity(intent);*/

                System.out.println(text + "\n" + devID);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
