import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by pranay on 2/23/2015.
 */
public class dbAcceptor implements Runnable{


    //public static String dbReceived = System.getProperty("user.dir");
    public static String dbReceived = "P:\\Masters Courses\\Mobile Sensing\\sqlite3\\";
    InputStream is = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    PrintWriter toClient = null;
    private Socket socket;
    dbAcceptor (Socket socket)
    {

        this.socket = socket;
     /*   try {

            System.out.println("In Try block for socket constructor.");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    Connection receivedConnect = null;
    byte[] aByte = new byte[6022386];
    int bytesRead, current;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    public  void run(){

            try {
                System.out.println("inside try block in dbAcceptor run.");
                is = socket.getInputStream();



                //dbReceived += "\\" + System.currentTimeMillis() + "AndroidTest.db";
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

               // System.out.println("File " + dbReceived + " Downloaded, " + current + " bytes transferred.");

                System.out.println("received file " + currentdbname);

                bos.close();
                fos.close();

               // dbMerger transferDB = new dbMerger(socket);
                MergeWindowFinal transferDB = new MergeWindowFinal(socket);
                Thread transferDBThread = new Thread(transferDB);
                transferDBThread.start();


                boolean dbTransferConfirm;
                toClient = new PrintWriter(socket.getOutputStream(), true);

                while(true){
                    //dbTransferConfirm = dbMerger.transferConfirmation;
                    dbTransferConfirm = MergeWindowFinal.transferConfirmation;

                    if(dbTransferConfirm) {

                        toClient.println(MergeWindowFinal.lastTSAfterMerge);
                        toClient.println(MergeWindowFinal.lastMACAfterMerge);
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



