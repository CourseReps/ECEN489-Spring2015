import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ChatClient {

    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog(
                "Enter IP Address of a machine that is\n running the date service on port 9090:");

        //String serverAddress = "127.0.0.1";
        //String serverAddress = "172.16.1.150";
        Scanner scanner = new Scanner(System.in);

        //Create a socket object on the address given
        Socket s = new Socket(serverAddress, 9090);

        //Create input and output streams
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        //Read the first message from the server
        String answer = input.readLine();

        System.out.println("Server says: " + answer);

        String msg = "";


        //Chat with the server
        while (!msg.equals("quit")) {
            System.out.print(">> ");
            msg = scanner.nextLine();
            out.println(msg);

            System.out.println("Waiting for reply from server...");

            answer = input.readLine();
            System.out.println("Server says: " + answer);
        }

        //Close the streams
        out.close();
        input.close();
        System.exit(0);
    }
}