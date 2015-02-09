
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

/**
 * Trivial client for the date server.
 */
public class Client implements Serializable {

    public static void main(String[] args) throws IOException {

        DateFormat Date = new SimpleDateFormat("EEEEEEEEEEEE, yyyy/MM/dd"); //This line specifies the date format
        Date dateObj = new Date();                                          //Formats the date
        DateFormat  Time= new SimpleDateFormat("HH:mm:ss");                 //This line specifies the time format
        Date timeObj = new Date();                                          //Formates the time
//The following is a list array thea is populated with pertinent system info
        List<String> systemInfo = Arrays.asList("Client Date: "+Date.format(dateObj),"Client Time: "+Time.format(timeObj),
                "Client User Name: "+System.getProperty("user.name"), "Client System Architecture: "+System.getProperty("os.arch"),
                "Client Operating System: "+System.getProperty("os.name"),"Client Operating System Version: "+System.getProperty("os.version"),
                "Client User's Home Directory: "+System.getProperty("user.home"), "Client Java Directory: "+System.getProperty("java.home"),
                "Java Vendor Name: "+System.getProperty("java.vendor"),"Client's Java Version: "+System.getProperty("java.version"));

        String serverAddress = JOptionPane.showInputDialog(      //displays the greeting and IP for the server
                "Enter IP Address of a machine that is\n" +
                        "running the service on port 9000:");
        Socket socketNew = new Socket(serverAddress, 9000);      //creates new socket
        ObjectOutput output = new ObjectOutputStream(socketNew.getOutputStream());  //constructs output stream for the system info
        output.writeObject(systemInfo); //sends the system info list into output stream


        System.exit(0); //exits the program
    }
}