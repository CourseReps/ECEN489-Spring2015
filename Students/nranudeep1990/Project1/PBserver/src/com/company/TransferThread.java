package com.company;

import javax.microedition.io.StreamConnection;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import java.io.DataOutputStream;

/**
 * Created by tungala on 2/25/2015.
 */
public class TransferThread implements Runnable {

    private StreamConnection streamConnection;

    public TransferThread(StreamConnection streamConnection) {

        streamConnection = streamConnection;
    }

    @Override
    public void run() {
        try {

            ClientSession clientSession = (ClientSession)streamConnection;
            HeaderSet headerSet = clientSession.createHeaderSet();
            clientSession.connect(headerSet);
            headerSet = clientSession.createHeaderSet();
            headerSet.setHeader(HeaderSet.NAME, "Test");
            headerSet.setHeader(HeaderSet.TYPE, ".db");

            Operation operation = clientSession.put(headerSet);

            DataOutputStream dataOutputStream = operation.openDataOutputStream();
            dataOutputStream.write("Hi".getBytes());

            operation.close();
            ((ClientSession) streamConnection).disconnect(headerSet);


        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

}
