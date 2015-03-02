import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

                dbReceived = System.getProperty("user.dir") + "\\" + System.currentTimeMillis() + "AndroidTest.db";

                fos = new FileOutputStream(dbReceived);
                //bos = new BufferedOutputStream(fos);
                System.out.println("before bytesRead.");

                System.out.println("after bytesRead.");
                //current = bytesRead;
                System.out.println("Server is Still Running, bytesRead Complete, input stream is not null.");

                System.out.println("bytesRead: " + bytesRead);

                System.out.println("Server is Still Running, exited bytesRead != 1 loop.");
                readFileAmt = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));
                int fileLength = Integer.parseInt(readFileAmt.readLine());
                System.out.println(fileLength);
                int length = 0;
                byte[] b = new byte[fileLength];

                current = 0;
                while (current<fileLength) {
                    length = is.read(b);
                    fos.write(b, 0, length);
                    current+=length;
                }


                System.out.println("File " + dbReceived + " Downloaded, " + current + " bytes transferred.");

                fos.close();

                dbMerger transferDB = new dbMerger();
                transferDB.dbMerge();
                dbTransferConfirm = dbMerger.transferConfirmation;

                    if (dbTransferConfirm) {

                        toClient.println(String.valueOf(dbMerger.lastRowID));
                        System.out.println("File Transfer Confirmed.");

                    }

                toClient.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.print(ex.getMessage());
            }

        }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }




    }



