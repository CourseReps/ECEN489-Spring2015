import java.io.*;
import java.net.Socket;

public class dbAcceptor implements Runnable{

    InputStream is = null;
    OutputStream os = null;
    PrintWriter toClient = null;
    private Socket socket;

    dbAcceptor (Socket socket) {
        this.socket = socket;
    }

    byte[] aByte = new byte[6022386];
    int bytesRead, current;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    public  void run(){
            try {
                System.out.println("inside try block in dbAcceptor run.");
                is = socket.getInputStream();

                fos = new FileOutputStream("D:\\Softwares\\SQLite\\AndroidTest.db");
                bos = new BufferedOutputStream(fos);
                System.out.println("before bytesRead.");

                bytesRead = is.read(aByte, 0, aByte.length);

                System.out.println("after bytesRead.");
                current = bytesRead;
                System.out.println("Server is Still Running, bytesRead Complete, input stream is not null.");

                do {
                    bytesRead = is.read(aByte, 0, (aByte.length - current));
                    if(bytesRead >= 0)
                        current += bytesRead;
                } while (bytesRead > -1);

                System.out.println("Server is Still Running, exited bytesRead != 1 loop.");

                bos.write(aByte, 0, current);
                System.out.print(aByte);
                bos.flush();

                System.out.println("File Downloaded, " + current + "bytes transferred.");


                dbMerger transferDB = new dbMerger(socket);
                Thread transferDBThread = new Thread(transferDB);
                transferDBThread.start();
                boolean dbTransferConfirm;
                toClient = new PrintWriter(socket.getOutputStream(), true);
                while(true){
                    dbTransferConfirm = dbMerger.transferConfirmation;

                    if(dbTransferConfirm) {

                        toClient.println(dbMerger.lastTimeStamp);
                        toClient.println(dbMerger.lastPBID);
                        System.out.println("File Transfer Confirmed.");
                        bos.close();
                        fos.close();
                        socket.close();
                        break;
                    }
                        Thread.sleep(100);
                }


            } catch (IOException ex) {
                ex.printStackTrace();
                // Do exception handling
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }



