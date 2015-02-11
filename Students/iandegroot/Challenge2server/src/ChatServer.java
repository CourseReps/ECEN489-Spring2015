import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.net.InetAddress;

public class ChatServer {

    public static void main(String[] args) throws IOException {
        String reply = "";
        String msgOut;

        //Create a new server socket
        ServerSocket listener = new ServerSocket(9090);

        Scanner scanner = new Scanner(System.in);

        //Output the IP address to connect to the server
        InetAddress ipAddress = InetAddress.getLocalHost();
        System.out.println("Current IP Address: " + ipAddress.getHostAddress());

        try {
            //Wait for a client to connect
            System.out.println("Waiting for client to connect...");
            Socket socket = listener.accept();
            System.out.println("Client connected from: " + socket.getLocalAddress().getHostName());

            //Create streams for reading and writing
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("Welcome to my chat room! Send 'quit' when you want to leave");

            //Chat with the client
            try {
                while (!reply.equals("quit")) {

                    System.out.println("Waiting for message from client...");

                    reply = input.readLine();
                    System.out.println("Client said: " + reply);

                    System.out.print(">> ");
                    msgOut = scanner.nextLine();
                    out.println(msgOut);
                }
            }
            finally {
                //Close the streams and the socket
                out.close();
                input.close();
                socket.close();
                System.out.println("Socket was closed");
            }
        }
        finally {
            //Close the server socket
            listener.close();
            System.out.println("ServerSocket was closed");
        }
    }
}