
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        //console used to enter server IP address
        String serverAddress = JOptionPane.showInputDialog("Enter IP Address of server");

        try {
            //create new socket object using server IP address and port
            Socket socket = new Socket(serverAddress, 9090);
            System.out.println("Server Connected! Please wait for server response...");
            //create input and output stream reader and writer objects
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);



            String question = input.readLine();
            int selection = JOptionPane.showConfirmDialog(null, question, "Make selection", JOptionPane.YES_NO_OPTION);
            output.println(selection); //sends the yes or no
            String answer = input.readLine();
            JOptionPane.showConfirmDialog(null, answer, "Results!", JOptionPane.DEFAULT_OPTION);


            //close input and output streams as well as socket and exit the chat program
            input.close();
            output.close();
            socket.close();
            System.exit(0);
        }
        //exception handling for connection time out
        catch (SocketException se) {
            System.out.println("Host unreachable! Please try again later.");
            System.exit(-1);
        }
    }
}
