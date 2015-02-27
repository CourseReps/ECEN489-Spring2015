package com.company;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;
import java.io.*;

/**
 * Created by tungala on 2/26/2015.
 */
public class ServerImplementation implements Runnable {

    StreamConnection connection;

    public ServerImplementation(StreamConnection connection) {
        this.connection = connection;
    }

    public void run(){
        try {
            RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
            System.out.println("Remote device address: "+dev.getBluetoothAddress());
            System.out.println("Remote device name: "+dev.getFriendlyName(true));

            //send response to spp client
            OutputStream outStream=connection.openOutputStream();
            InputStream inStream=connection.openInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outStream));
            String recv = bufferedReader.readLine();
            System.out.println("Android device ID is " + recv);
            //long androidID = Long.parseLong(recv);

            // Send the android device my ID
//                writeOutStream.write(String.valueOf(parent.getDB().getMyID()) + "\n");
//                long clientID = Math.abs(new HighQualityRandom().nextLong());
//            bufferedWriter.write("PB2" + "\n");
//            bufferedWriter.flush();

//                tmpOut.flush();

            // CHECK client ID and see what the latest DB line they have

            String lastPbTime = bufferedReader.readLine();
//                long latestLine = Long.parseLong(recv);
            System.out.println("Most recent DB line received by client is: " + lastPbTime);

            // CHECK client ID and see what the latest DB line they sent to svr
            String lastSvrTime = bufferedReader.readLine();
//                long latestLine = Long.parseLong(recv);
            System.out.println("Most recent DB line sent to SVR by client is: " + lastSvrTime);


            ///// CODE TO CREATE NEW DB CHUNK BEFORE SENDING IT TO R2DATA/////////

//            DBHandler dbHandler = new DBHandler(lastPbTime,lastSvrTime);
//            dbHandler.createTransferDB();
//
//            String lastTxTime = dbHandler.getLastTxTime().toString();
//            bufferedWriter.write(lastTxTime + "\n");
//            bufferedWriter.flush();


            // Do some processing to build the DB for transmission and calculate the file size
            File db = new File(".//prombox.db");
//                writeOutStream.write(String.valueOf(db.length()) + "\n");
//                writeOutStream.flush();
            System.out.println("File length I will send is " + String.valueOf(db.length()));

//                readInStream.close();
//                writeOutStream.close();

            /////////////////////////////////////////////////////////////////////////////////
            // BEGIN DOWNLOAD CODE //////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////
            FileInputStream fileIn = new FileInputStream(db);
            BufferedInputStream bufFile = new BufferedInputStream(fileIn);
            int downloadCounter = 0;
            boolean streamsOpen = false;

            int filesize = (int) db.length();
            String filename = "testDB";

            try {

                byte[] b = new byte[filesize];
                bufFile.read(b, 0, b.length);
                int length;

                streamsOpen = true;
//                    while ((length = fileIn.read()) != -1) {
                outStream.write(b, 0, filesize);
                outStream.flush();
//                        downloadCounter += length;

//                        parent.newMessage("Uploading: " + downloadCounter + "/" + filesize);
                System.out.println("Uploading database file");
//                    }

            } catch (FileNotFoundException fnfe) {
                System.out.println("Bluetooth: " + fnfe.getMessage());
            } catch (IOException ioe) {
                System.out.println("Bluetooth: " + ioe.getMessage());
            } catch (Exception e) {
                if (streamsOpen) {
                    System.out.println("Bluetooth: " + e.getMessage());
                }
                System.out.println("The current download has been stopped by another process");
            } finally {
                if (streamsOpen) {
                    try {
                        fileIn.close();
                    } catch (Exception e) {
                        System.out.println("Bluetooth: " + e.getMessage());
                    }
                }
            }

            /////////////////////////////////////////////////////////////////////////////////
            // END DOWNLOAD CODE ////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////

            System.out.println("Closing all connections and ending program...");
            inStream.close();
            outStream.close();
            connection.close();


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
