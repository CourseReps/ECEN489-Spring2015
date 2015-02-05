//Example 26 (updated)

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Scanner;


/*
 * A chat server that delivers public and private messages.
 */
public class Server {

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    // This chat server can accept up to maxClientsCount clients' connections.
    private static int maxClientsCount = 10;
    private static clientThread[] threads;
    private static CharBuffer msg = CharBuffer.allocate(256);
    public static void main(String args[]) {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            byte[] ipaddr= addr.getAddress();
            System.out.println(addr.getHostAddress());
            System.out.println(addr.getHostName());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // The default port number.
        int portNumber = 2222;
        msg.put("Sample Message");
        if (args.length < 3) {
            System.out.println("Not enough arugments. Reverting to defaults.\n Now using port number=" + portNumber + "\nMax client count = "+ maxClientsCount);
            System.out.print("Message = ");
            System.out.println(msg.array());
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
            maxClientsCount = Integer.valueOf(args[1]).intValue();
            Arrays.fill(msg.array(), '\0');
            msg.clear();
            msg.put(args[2].toString());
        }
        threads = new clientThread[maxClientsCount];
    /*
     * Open a server socket on the portNumber (default 2222). Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
     */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

    /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads, msg)).start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. The thread broadcast the incoming messages to all clients and
 * routes the private message to the particular client. When a client leaves the
 * chat room this thread informs also all the clients about that and terminates.
 */
class clientThread extends Thread {

    // private String clientName = null;
    private BufferedReader is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private CharBuffer msg;
    private final clientThread[] threads;
    private int maxClientsCount;

    public clientThread(Socket clientSocket, clientThread[] threads, CharBuffer msg) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;
        String inputString;
        String clientString = "";
        try {

            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());

            while(!clientString.equals("\\disconnect")){
                clientString = is.readLine();
                System.out.println("Client Response:");
                System.out.println(clientString);
                System.out.println("Server Response:" );
                inputString = input.nextLine();
                os.println(inputString);
            }

            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}