import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by kwilk_000 on 2/23/2015.
 */
public class dbAcceptor{

    public static String dbReceived = System.getProperty("user.dir");
    InputStream is = null;
    BufferedInputStream bis = null;
    BufferedReader readFileAmt = null;
    OutputStream os = null;
    PrintWriter toClient = null;
    private Socket socket;
    boolean dbTransferConfirm;
    DataInputStream fileRead = null;

    dbAcceptor (Socket socket) {
        this.socket = socket;
    }

    Connection receivedConnect = null;
    byte[] aByte = new byte[6022386];
    int bytesRead = 0;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    public  void dbAccept() {

        int n = 0;
        String suffix = null;
        String read = null;
        try {

            toClient = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("R2DATA is sending " + n + " files.");


            try {
                is = socket.getInputStream();

                dbReceived = System.getProperty("user.dir") + "\\" + System.currentTimeMillis() + "AndroidTest.db";

                fos = new FileOutputStream(dbReceived);
                bos = new BufferedOutputStream(fos);

                System.out.println("bytesRead: " + bytesRead);



                bytesRead = is.read(aByte, 0, aByte.length);
                current = bytesRead;

                do {

                    bytesRead = is.read(aByte, current, (aByte.length - current));
                    if (bytesRead >= 0)
                        current += bytesRead;

                    System.out.println("current: " + current);
                } while (bytesRead > -1);
                System.out.println("Server is Still Running, exited bytesRead != 1 loop.");

                System.out.println("File " + dbReceived + " Downloaded, " + current + " bytes transferred.");


                bos.write(aByte, 0, current);
                bos.flush();

                dbMerger transferDB = new dbMerger();
                transferDB.dbMerge();
                dbTransferConfirm = dbMerger.transferConfirmation;

                    if (dbTransferConfirm) {

                        toClient.println(String.valueOf(dbMerger.lastRowID));
                        //toClient.println("PB" + dbMerger.lastPBID);
                        System.out.println("File Transfer Confirmed.");


                    }
                else System.out.println("Transfer not confirmed.  Check main database and retry transfer");

                toClient.flush();

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.print(ex.getMessage());
                // Do exception handling
            }

        //}
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
/*
            catch (InterruptedException e) {
                e.printStackTrace();
            }
*/

        }




    }



