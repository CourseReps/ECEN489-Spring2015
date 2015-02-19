package com.mandel.paramapp;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by mandel on 2/16/15.
 */
public class ServerPushService extends IntentService {
    public ServerPushService() {
        super("ServerPushService");
    }
    DBHelper mydb;
    int SERVERPORT = 2222;
    String SERVER_IP = "10.202.104.195";
    Socket socket;
    @Override
    protected void onHandleIntent(Intent workIntent) {
        mydb = new DBHelper(this);

        JSONArray jsonToSend = mydb.toJson();


    try {
        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
        socket = new Socket(serverAddr, SERVERPORT);
        PrintWriter out = new PrintWriter(new BufferedWriter(
        new OutputStreamWriter(socket.getOutputStream())),true);

        out.println(jsonToSend.toString());

    }
    catch(UnknownHostException e1){
        e1.printStackTrace();
    }
    catch(IOException e1){
        e1.printStackTrace();
    }
    catch(Exception e){
        e.printStackTrace();
    }


     mydb.updateToSent();

    System.out.println("Success");

        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();


    }
}
