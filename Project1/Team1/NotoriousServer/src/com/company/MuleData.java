package com.company;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by pranay on 2/23/2015.
 */
public class MuleData implements Runnable{


    //public static String

    public static String dbReceived;
    InputStream is = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    PrintWriter toClient = null;

    byte[] aByte;
    int bytesRead, current;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    private Socket socket;


    MuleData (Socket socket)
    {

        this.socket = socket;

    }



    public  void run(){

        dbReceived = "P:\\Masters Courses\\Mobile Sensing\\sqlite3\\";
        //dbReceived = System.getProperty("user.dir");
        aByte = new byte[6022386];

       // int n = 0;
       // String suffix = null;
       // String read = null;

        try {

            toClient = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //System.out.println("inside try block in dbAcceptor run.");
            is = socket.getInputStream();

            String currentdbname = System.currentTimeMillis() + "AndroidTest.db";
            dbReceived += "\\" + currentdbname ;

            fos = new FileOutputStream(dbReceived);
            bos = new BufferedOutputStream(fos);
            System.out.println("before bytesRead.");

            bytesRead = is.read(aByte, 0, aByte.length);

            System.out.println("starting reading of the bytes.");

            current = bytesRead;
            // System.out.println("Server is Still Running, bytesRead Complete, input stream is not null.");

            do {
                bytesRead = is.read(aByte, current, (aByte.length - current));
                if(bytesRead >= 0)
                    current += bytesRead;
            } while (bytesRead > -1);

            System.out.println("Server is Still Running, exited bytesRead != 1 loop.");

            bos.write(aByte, 0, current);
            System.out.print(aByte);
            bos.flush();

            System.out.println("received file " + currentdbname);

            bos.close();
            fos.close();

            MergeWindow transferDB = new MergeWindow(socket);
            Thread transferDBThread = new Thread(transferDB);
            transferDBThread.start();


            boolean dbTransferConfirm;
            toClient = new PrintWriter(socket.getOutputStream(), true);

            while(true){

                dbTransferConfirm = MergeWindow.transferConfirmation;

                if(dbTransferConfirm) {

                    // toClient.println(MergeWindowFinal.lastTSAfterMerge);
                    // toClient.println(MergeWindowFinal.lastMACAfterMerge);
                    toClient.println(MergeWindow.lastIDAfterMerge);
                    System.out.println("File Transfer Confirmed.");
                    socket.close();
                    break;

                }
                Thread.sleep(100);
            }
            socket.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            // Do exception handling
        }
/*
            catch (InterruptedException e) {
                e.printStackTrace();
            }
*/

    }



}



