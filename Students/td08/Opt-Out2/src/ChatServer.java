
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;


public class ChatServer {
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) throws IOException {
        //create ServerSocket object
        try {
            ServerSocket server = new ServerSocket(9090);
            InetAddress ipAddress = InetAddress.getLocalHost();
            System.out.println("Current IP Address: " + ipAddress.getHostAddress());
            Scanner scan = new Scanner(System.in);
            //create new socket for client connection
            try {
                Socket socket = server.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                System.out.println("Client Connected!");
                output.println("Welcome to the chat! You are connected on port " + server.getLocalPort());
                //Server begins chat with client
                System.out.println("Type initial message to client below:");
                String sent = scan.nextLine();
                output.println(sent);
                while (true) {
                    //exchange of information between server and client. Message is sent, then wait to receive
                    try {
                        String received = input.readLine();
                        if (received.equals("EXIT")) {
                            System.out.println("Client terminated connection. Closing chat...");
                            break;
                        }
                        System.out.println(RED + "Client says:\n" + received + RESET);
                        System.out.println(BLUE + "Type response below or 'EXIT' to close chat:" + RESET);
                        sent = scan.nextLine();
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
                //close input and output streams as well as socket
                input.close();
                output.close();
                socket.close();
            } finally {
                server.close();
            }
        }
        //exception handling for port handling issues
        catch (SocketException noPort)
        {
            System.out.println("Could not establish server. Port may be unavailable.");
        }
    }
}
