package com.company;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * Created by tungala on 2/25/2015.
 */
public class ConnThread implements Runnable {

    public ConnThread() {

    }

    @Override
    public void run() {
        getConnection();
    }

    private void getConnection() {
        LocalDevice localDevice = null;
        StreamConnectionNotifier streamConnectionNotifier = null;
        StreamConnection streamConnection = null;

        try {

            localDevice = localDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);

            UUID uuid = new UUID(0x1106);

            String url = "btgoep://localhost:"+uuid.toString()+ ";name=ObexExample";
            streamConnectionNotifier = (StreamConnectionNotifier) Connector.open(url);

        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }

        while(true) {
            try {
                System.out.println("Waiting for Connection....");
                streamConnection = streamConnectionNotifier.acceptAndOpen();
                RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(streamConnection);
                System.out.println("Connected to :"+remoteDevice.getBluetoothAddress());

                Thread transferThread = new Thread(new TransferThread(streamConnection));
                transferThread.start();
            }
            catch(Exception e) {
                e.printStackTrace();
                return;
            }
        }

    }
}
