
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException{

        String[] systemInfoArray = new String [10]; //this is an array of strings for the system info
        int i = 0;  //int for the counter and for the array

        InetAddress ipAddress = InetAddress.getLocalHost();
        System.out.println("Current Server IP Address: "+ipAddress.getHostAddress());   //prints the array in a window
        ServerSocket listener = new ServerSocket(9000);  //creates new server socket on port 9000
        Socket socket = listener.accept();  //listens for the accept from client
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream()); //creates input stream from socket
        String bla = (String)objectInputStream.readObject();
        System.out.println(bla);

    }
}