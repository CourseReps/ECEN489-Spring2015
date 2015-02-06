/**
 * Created by Benito on 2/4/2015.
 */
/*import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class Server {

        public static void main(String[] args) {
            // Code to print Date and Time
            System.out.println("Date and Time:");
            Date date = new Date();
            System.out.println(date.toString());
            // Printing System Properties
            System.out.println("System Properties:");
            System.out.println("Java Class path: "+ System.getProperty("java.class.path"));
            System.out.println("JRE vendor name: "+ System.getProperty("java.vendor"));
            System.out.println("OS architecture: "+ System.getProperty("os.arch"));
            System.out.println("OS name: "+ System.getProperty("os.name"));
            System.out.println("OS version: "+ System.getProperty("os.version"));
            System.out.println("User home directory: "+ System.getProperty("user.home"));
            System.out.println("User account name: "+ System.getProperty("uer.name"));
        }
    }*/
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class Server {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString());
                } finally {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
}