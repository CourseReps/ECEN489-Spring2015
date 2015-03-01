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
    //BufferedInputStream bis = null;
    BufferedReader readFileAmt = null;
    OutputStream os = null;
    PrintWriter toClient = null;
    private Socket socket;
    boolean dbTransferConfirm;
    DataInputStream fileRead = null;

    dbAcceptor (Socket socket) {
        this.socket = socket;
     /*   try {
            System.out.println("In Try block for socket constructor.");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    Connection receivedConnect = null;
    byte[] aByte = new byte[6022386];
    int bytesRead = 0;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    public  void dbAccept() {


        //TODO RECEIVE INT FROM ANDROID STATING NUMBER OF FILES TO BE SENT
        int n = 0;
        String suffix = null;
        String read = null;
        try {
            /*readFileAmt = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));*/
           /* fileRead = new DataInputStream(socket.getInputStream());*/
            //read = readFileAmt.readLine();
           // n = Integer.parseInt(read);
      /*      n = fileRead.readInt();
            readFileAmt.close();*/
            toClient = new PrintWriter(socket.getOutputStream(), true);
            n = DBFileWriter.numberFiles;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("R2DATA is sending " + n + " files.");


        for (int i = 0; i < n; i++) {
            switch (i) {
                case 1:
                    suffix = "st";
                case 2:
                    suffix = "nd";
                case 3:
                    suffix = "rd";
                default:
                    suffix = "th";
            }

            try {
                System.out.println("Writing the " + i + suffix + " file.");
                is = socket.getInputStream();

                /*try {
                    Class.forName("org.sqlite.JDBC");
                    receivedConnect = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\kwilk_000\\Documents\\SQLite\\123_AndroidTest.db"); //connects to received database
                    receivedConnect.setAutoCommit(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Opened database successfully");*/

                dbReceived = System.getProperty("user.dir") + "\\" + System.currentTimeMillis() + "AndroidTest.db";

                fos = new FileOutputStream(dbReceived);
                //bos = new BufferedOutputStream(fos);
                System.out.println("before bytesRead.");

                //bytesRead = is.read(aByte, 0, aByte.length);

                System.out.println("after bytesRead.");
                //current = bytesRead;
                System.out.println("Server is Still Running, bytesRead Complete, input stream is not null.");

                System.out.println("bytesRead: " + bytesRead);
                /*do {
//                    bytesRead = is.read(aByte, 0, (aByte.length - current));
                    bytesRead = is.read(aByte, current, (aByte.length - current));
                    if (bytesRead >= 0)
                        current += bytesRead;
                    System.out.println("bytesRead: " + bytesRead);
//                    System.out.println("current: " + current);
                } while (bytesRead > -1);*/
                System.out.println("Server is Still Running, exited bytesRead != 1 loop.");
                readFileAmt = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));
                int fileLength = Integer.parseInt(readFileAmt.readLine());
                System.out.println(fileLength);
                int length = 0;
                byte[] b = new byte[fileLength];
//                byte[] b = new byte[2048];
                current = 0;
//                while ((length = is.read(b)) != -1) {
                while (current<fileLength) {
                    //bos.write(aByte, 0, current);
                    length = is.read(b);
                    fos.write(b, 0, length);
                    current+=length;
                    //System.out.println(current);
                }
                //System.out.print(aByte);
               // bos.flush();

                System.out.println("File " + dbReceived + " Downloaded, " + current + " bytes transferred.");

                //bos.close();
                fos.close();

                dbMerger transferDB = new dbMerger();
                transferDB.dbMerge();
                dbTransferConfirm = dbMerger.transferConfirmation;

                    if (dbTransferConfirm) {

                        toClient.println(String.valueOf(dbMerger.lastRowID));
                        //toClient.println("PB" + dbMerger.lastPBID);
                        System.out.println("File Transfer Confirmed.");
                        //socket.close();
                        //break;
                      /*  boolean success = (new File(dbReceived)).delete();
                        if (success) {
                            System.out.println("The file has been successfully deleted");
                        }*/

                    }

                toClient.flush();
                // socket.close();
               // is.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.print(ex.getMessage());
                // Do exception handling
            }

        }
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



