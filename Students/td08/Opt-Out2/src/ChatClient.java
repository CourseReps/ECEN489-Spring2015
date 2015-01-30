
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClient {
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter IP Address of server:");
        //console used to enter server IP address
        String serverAddress = scan.nextLine();

        try {
            //create new socket object using server IP address and port
            Socket socket = new Socket(serverAddress, 9090);
            System.out.println("Server Connected! Please wait for server response...");
            //create input and output stream reader and writer objects
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            //wait for initial server welcome message
            String init = input.readLine();
            System.out.println(init);
            while (true) {
                //exchange of information between client and server. Message is sent, then wait to receive
                try {
                    String received = input.readLine();
                    if (received.equals("EXIT")) {
                        System.out.println(RED + "Server terminated connection. Closing chat..." + RESET);
                        break;
                    }
                    System.out.println(RED + "Server says:\n" + RESET + received);
                    System.out.println(BLUE + "Type response below or 'EXIT' to close chat:" + RESET);
                    String sent = scan.nextLine();
                    output.println(sent);
                    //if exit command is sent or received, chat is closed
                    if (sent.equals("EXIT")) {
                        System.out.println("Goodbye!");
                        break;
                    }
                } catch (SocketException se) {
                    System.out.println("Unexpected termination...");
                }
            }
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
