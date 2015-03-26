
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

        while (true){
        InetAddress ipAddress = InetAddress.getLocalHost();
        System.out.println("Current Server IP Address: "+ipAddress.getHostAddress());   //prints the array in a window
        ServerSocket listener = new ServerSocket(9000);  //creates new server socket on port 9000
        Socket socket = listener.accept();  //listens for the accept from client
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        //ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream()); //creates input stream from socket
        BufferedReader readerB = new BufferedReader(reader);
        String bla = readerB.readLine();
        System.out.println(bla);
        }


    }
}