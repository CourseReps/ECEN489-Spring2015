
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class Server {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException{

        InetAddress ipAddress = InetAddress.getLocalHost();
        System.out.println("Current Server IP Address: "+ipAddress.getHostAddress());   //prints the array in a window
        ServerSocket listener = new ServerSocket(9000);  //creates new server socket on port 9000

        while(true) {
            Socket socket = listener.accept();  //listens for the accept from client
            InputStreamReader reader = new InputStreamReader(socket.getInputStream());
            BufferedReader readerB = new BufferedReader(reader);
            String bla = readerB.readLine();
            System.out.println(bla);
        }


    }
}