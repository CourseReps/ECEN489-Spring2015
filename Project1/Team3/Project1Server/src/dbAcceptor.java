import java.io.*;
import java.net.Socket;

/**
 * Created by kwilk_000 on 2/23/2015.
 */
public class dbAcceptor implements Runnable{

    InputStream is = null;
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


    byte[] aByte = new byte[6022386];
    int bytesRead, current;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    public  void run(){



            try {
                System.out.println("inside try block in dbAcceptor run.");
                is = socket.getInputStream();

                fos = new FileOutputStream("D:\\Softwares\\SQLite\\sqlite-shell-win32-x86-3080801\\" +System.currentTimeMillis() + "AndroidTest.db");
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

                bos.close();
                fos.close();
                socket.close();

            } catch (IOException ex) {
                ex.printStackTrace();
                // Do exception handling
            }

        }



    }



