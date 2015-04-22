package com.example.joshuacano.androidsocketserver;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {

    private Socket socket;
    BufferedReader fromServer;

    private static final int SERVERPORT = 9898;
    private static final String SERVER_IP = "10.202.101.71";

    EditText et;
    Button connect;
    String serverResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.EditText01);
        connect = (Button) findViewById(R.id.connectButton);

        connect.setOnClickListener((v) -> {
            connect.setEnabled(false);
            new Client(getApplicationContext()).execute();

        });

    }

    public void onClick(View view) {
        try {
            new Client(getApplicationContext()).execute();

        } catch (Exception e) {}
    }


    class Client extends AsyncTask<Void,Void,Void> {
        Context context;

        public Client(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

                JSONObject object = new JSONObject();
                object.put("timestamp","123456789");

                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                String sentString = object.toString();
                out.println(sentString);

                fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                serverResponse = fromServer.readLine();

                socket.close();


            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            et.setText(serverResponse);
            connect.setEnabled(true);


        }

    }
}
