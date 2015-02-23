import java.io.*;
import java.net.Socket;

public class TestClient {

    public final static int SOCKET_PORT = 13267;      // you may change this
    public final static String SERVER = "127.0.0.1";  // localhost
    //public final static String FILE_TO_RECEIVED = System.getProperty("user.dir") + "\\testing.txt";

    public final static String FILE_TO_SEND = System.getProperty("user.dir") + "\\test.db";  // you may change this


    // should bigger than the file to be downloaded

    public static void main (String [] args ) throws IOException {
        int bytesRead;
        int current;

        System.out.println();
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        Socket sock = null;
        try {
            sock = new Socket(SERVER, SOCKET_PORT);
            System.out.println("Connecting...");

            // send file
            File myFile = new File (FILE_TO_SEND);
            byte [] mybytearray  = new byte [(int)myFile.length()];
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            os = sock.getOutputStream();
            System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            System.out.println("Done.");
/*
            // receive file
            byte [] mybytearray  = new byte [FILE_SIZE];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(FILE_TO_RECEIVED);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println("File " + FILE_TO_RECEIVED
                    + " downloaded (" + current + " bytes read)");
                    */
        }
        finally {
            if (bis != null) bis.close();
            if (os != null) os.close();
            if (sock != null) sock.close();
        }
    }

}