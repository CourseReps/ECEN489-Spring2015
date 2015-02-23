import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    public final static int SOCKET_PORT = 13267;  // you may change this
    public static String FILE_TO_RECEIVE = System.getProperty("user.dir");
    public final static int FILE_SIZE = 6022386; // file size temporary hard coded

    public static void main (String [] args ) throws IOException {
        int current, bytesRead;

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ServerSocket servsock = null;
        Socket sock = null;

        try {
            servsock = new ServerSocket(SOCKET_PORT);
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);

                    //Timestamp the file
                    //FILE_TO_RECEIVE += "\\" + System.currentTimeMillis() + sock.getInetAddress().getCanonicalHostName();
                    FILE_TO_RECEIVE += "\\" + System.currentTimeMillis() + "_testing.db";

                    // receive file
                    byte[] mybytearray = new byte[FILE_SIZE];
                    InputStream is = sock.getInputStream();
                    fos = new FileOutputStream(FILE_TO_RECEIVE);
                    bos = new BufferedOutputStream(fos);
                    bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    current = bytesRead;

                    do {
                        bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                        if (bytesRead >= 0) current += bytesRead;
                    } while (bytesRead > -1);

                    bos.write(mybytearray, 0, current);
                    bos.flush();
                    System.out.println("File " + FILE_TO_RECEIVE + " downloaded (" + current + " bytes read)");

                    FILE_TO_RECEIVE = System.getProperty("user.dir");
                }
                finally {
                    if (fos != null) fos.close();
                    if (bos != null) bos.close();
                    if (sock!=null) sock.close();
                }
            }
        }
        finally {
            if (servsock != null) servsock.close();
        }
    }
}
